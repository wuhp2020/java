package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.model.Group;
import org.ssssssss.magicapi.core.model.PathMagicEntity;
import org.ssssssss.magicapi.core.model.TreeNode;
import org.ssssssss.magicapi.core.service.MagicResourceService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MagicApiService {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    private List<IMagicApiService> iMagicApiServices;

    @Transactional(rollbackFor = Exception.class)
    public void reset() {
        this.deleteGroupsFiles();
        this.createGroupsFiles();
    }

    /**
     * 删除自动创建的组、接口
     */
    private void deleteGroupsFiles() {
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        Map<String, TreeNode<Group>> groupMap = magicResourceService.tree();
        Optional.ofNullable(groupMap).orElse(Collections.emptyMap()).forEach((k, v) -> {
            Optional.ofNullable(v).ifPresent(treeNode -> {
                Optional.ofNullable(treeNode.flat()).orElse(Collections.emptyList()).stream().forEach(group -> {
                    if (group != null && "auto".equals(group.getUpdateBy())) {
                        List<PathMagicEntity> pathMagicEntityList = magicResourceService.listFiles(group.getId());
                        List<PathMagicEntity> autos = Optional.ofNullable(pathMagicEntityList).orElse(Collections.emptyList())
                                .stream().filter(apiInfo -> "auto".equals(apiInfo.getUpdateBy()))
                                .collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(pathMagicEntityList) && pathMagicEntityList.size() == autos.size()) {
                            magicResourceService.delete(group.getId());
                        }
                        autos.stream().forEach(autoApiInfo -> {
                            magicResourceService.delete(autoApiInfo.getId());
                        });
                    }
                });
            });
        });
    }

    /**
     * 自动创建组、接口、函数
     */
    private void createGroupsFiles() {
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        Map<String, TreeNode<Group>> groupTree = magicResourceService.tree();
        List<Group> groupList = new LinkedList<>();
        Optional.ofNullable(groupTree).orElse(Collections.emptyMap())
                .forEach((k,v) -> {
                    if (v != null) {
                        List<Group> groups = v.flat();
                        if (!CollectionUtils.isEmpty(groups)) {
                            groupList.addAll(groups);
                        }
                    }
                });
        Map<String, Group> groupMap = groupList.stream()
                .filter(group -> StringUtils.isNotEmpty(group.getPath()))
                .collect(Collectors.toMap(c -> c.getType() +"-"+ c.getPath(), e->e));

        ColumnMapRowMapper tableRowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> tables = jdbcTemplate.query("select * from information_schema.TABLES " +
                "where TABLE_SCHEMA=(select database())", tableRowMapper);

        Optional.ofNullable(tables).orElse(Collections.emptyList()).stream()
                .filter(table -> (!"magic_api".equals(table.get("TABLE_NAME")))).forEach(table -> {
            String tableName = ((String) table.get("TABLE_NAME"));

            ColumnMapRowMapper columnRowMapper = new ColumnMapRowMapper();
            List<Map<String, Object>> columnMap = jdbcTemplate.query("select * from information_schema.COLUMNS " +
                    "where TABLE_SCHEMA = (select database()) and TABLE_NAME='"+ tableName +"'", columnRowMapper);

            Optional.ofNullable(iMagicApiServices)
                    .orElse(Collections.emptyList()).stream().forEach(magicApiAbstractService -> {
                if ("function".equals(magicApiAbstractService.type())) {
                    Group group = groupMap.get("function-/" + tableName.replaceAll("_", ""));
                    magicApiAbstractService.autoCreate(group, tableName, columnMap);
                } else if ("api".equals(magicApiAbstractService.type())) {
                    Group group = groupMap.get("api-/" + tableName.replaceAll("_", ""));
                    magicApiAbstractService.autoCreate(group, tableName, columnMap);
                }
            });
        });
    }
}
