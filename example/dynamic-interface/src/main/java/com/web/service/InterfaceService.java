package com.web.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.web.convert.MasterDataMapper;
import com.web.entity.InterfaceEntity;
import com.web.entity.InterfaceObjectParamRelationEntity;
import com.web.entity.InterfaceParamEntity;
import com.web.mapper.InterfaceMapper;
import com.web.mapper.InterfaceObjectParamRelationMapper;
import com.web.mapper.InterfaceParamMapper;
import com.web.mapper.TableMapper;
import com.web.vo.InterfaceObjectParamRelationResVO;
import com.web.vo.InterfaceParamReqVO;
import com.web.vo.InterfaceParamResVO;
import com.web.vo.InterfaceReqVO;
import com.web.vo.InterfaceResVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/13
 * @ Desc   : 描述
 */
@Service
@Slf4j
public class InterfaceService {

    @Autowired
    InterfaceMapper interfaceMapper;

    @Autowired
    InterfaceParamMapper interfaceParamMapper;

    @Autowired
    InterfaceObjectParamRelationMapper interfaceObjectParamRelationMapper;

    @Autowired
    TableMapper tableMapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveInterface(InterfaceReqVO reqVO) {

        Wrapper<InterfaceEntity> wrapper = Wrappers.<InterfaceEntity>query()
                .lambda().eq(InterfaceEntity::getUrl, reqVO.getUrl());
        List<InterfaceEntity> list = interfaceMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("接口路径已存在，请更换url或删除后再新建");
        }

        // 1 Params, 2 Body, 3 header, 4 cookie, 5 auth, 6 result
        InterfaceEntity interfaceEntity = MasterDataMapper.INSTANCES.masterInterfaceReqVO2Entity(reqVO);
        String interfaceId = SnowflakeIdWorker.getId();
        interfaceEntity.setId(interfaceId);
        interfaceMapper.insert(interfaceEntity);

        // Params
        Optional.ofNullable(reqVO.getParams()).orElse(Collections.emptyList()).stream().forEach(param -> {
            InterfaceParamEntity interfaceParam = MasterDataMapper.INSTANCES.masterInterfaceParamReqVO2Entity(param);
            interfaceParam.setId(SnowflakeIdWorker.getId());
            interfaceParam.setParamType("1");
            interfaceParam.setInterfaceId(interfaceId);
            interfaceParamMapper.insert(interfaceParam);
        });

        // Body
        Optional.ofNullable(reqVO.getBody()).ifPresent(param -> {
            InterfaceParamEntity interfaceParam = MasterDataMapper.INSTANCES.masterInterfaceParamReqVO2Entity(param);
            String paramId = SnowflakeIdWorker.getId();
            interfaceParam.setId(paramId);
            interfaceParam.setParamType("2");
            interfaceParam.setInterfaceId(interfaceId);
            interfaceParamMapper.insert(interfaceParam);
            this.childrenInterfaceParam(interfaceId, paramId, "2", param.getChildren());
        });

        // header
        Optional.ofNullable(reqVO.getHeader()).orElse(Collections.emptyList()).stream().forEach(param -> {
            InterfaceParamEntity interfaceParam = MasterDataMapper.INSTANCES.masterInterfaceParamReqVO2Entity(param);
            interfaceParam.setId(SnowflakeIdWorker.getId());
            interfaceParam.setParamType("3");
            interfaceParam.setInterfaceId(interfaceId);
            interfaceParamMapper.insert(interfaceParam);
        });

        // cookie
        Optional.ofNullable(reqVO.getCookie()).orElse(Collections.emptyList()).stream().forEach(param -> {
            InterfaceParamEntity interfaceParam = MasterDataMapper.INSTANCES.masterInterfaceParamReqVO2Entity(param);
            interfaceParam.setId(SnowflakeIdWorker.getId());
            interfaceParam.setParamType("4");
            interfaceParam.setInterfaceId(interfaceId);
            interfaceParamMapper.insert(interfaceParam);
        });

        // auth
        Optional.ofNullable(reqVO.getAuth()).orElse(Collections.emptyList()).stream().forEach(param -> {
            InterfaceParamEntity interfaceParam = MasterDataMapper.INSTANCES.masterInterfaceParamReqVO2Entity(param);
            String paramId = SnowflakeIdWorker.getId();
            interfaceParam.setId(paramId);
            interfaceParam.setParamType("5");
            interfaceParam.setInterfaceId(interfaceId);
            interfaceParamMapper.insert(interfaceParam);
        });

        // result
        Optional.ofNullable(reqVO.getResult()).ifPresent(param -> {
            InterfaceParamEntity interfaceParam = MasterDataMapper.INSTANCES.masterInterfaceParamReqVO2Entity(param);
            String paramId = SnowflakeIdWorker.getId();
            interfaceParam.setId(paramId);
            interfaceParam.setParamType("6");
            interfaceParam.setInterfaceId(interfaceId);
            interfaceParamMapper.insert(interfaceParam);
            this.childrenInterfaceParam(interfaceId, paramId, "6", param.getChildren());
        });
    }

    /**
     * 递归参数子集, 保存
     * @param interfaceId
     * @param parentId
     * @param paramType
     * @param children
     */
    private void childrenInterfaceParam(String interfaceId, String parentId, String paramType, List<InterfaceParamReqVO> children) {
        for (InterfaceParamReqVO paramReqVO: children) {
            InterfaceParamEntity interfaceParam = MasterDataMapper.INSTANCES.masterInterfaceParamReqVO2Entity(paramReqVO);
            String paramId = SnowflakeIdWorker.getId();
            interfaceParam.setId(paramId);
            interfaceParam.setParamType(paramType);
            interfaceParam.setInterfaceId(interfaceId);
            interfaceParam.setParentId(parentId);
            interfaceParamMapper.insert(interfaceParam);
            if (!CollectionUtils.isEmpty(paramReqVO.getChildren())) {
                if (!CollectionUtils.isEmpty(paramReqVO.getRelations())) {
                    Map<String, Map<String, Object>> sourceTableColumnNames =  this.sysTableColumn(paramReqVO.getRelations()
                            .get(0).getSourceTableName()).stream().collect(Collectors.toMap(e -> (String) e.get("column_name"), e -> (Map<String, Object>)e));

                    Map<String, Map<String, Object>> targetTableColumnNames = this.sysTableColumn(paramReqVO.getRelations()
                            .get(0).getTargetTableName()).stream().collect(Collectors.toMap(e -> (String) e.get("column_name"), e -> (Map<String, Object>)e));

                    // 要建立关系
                    paramReqVO.getRelations().stream().forEach(relation -> {
                        Map<String, Object> sourceColumn = sourceTableColumnNames.get(relation.getSourceTableColumnName());
                        String sourceType = "";
                        if (sourceColumn.get("data_type") != null) {
                            sourceType = (String) sourceColumn.get("data_type");
                        }

                        Map<String, Object> targetColumn = targetTableColumnNames.get(relation.getTargetTableColumnName());
                        String targetType = "";
                        if (targetColumn.get("data_type") != null) {
                            targetType = (String) targetColumn.get("data_type");
                        }

                        InterfaceObjectParamRelationEntity paramRelation = MasterDataMapper.INSTANCES.masterInterfaceObjectParamRelationReqVO2Entity(relation);
                        paramRelation.setId(SnowflakeIdWorker.getId());
                        paramRelation.setInterfaceId(interfaceId);
                        paramRelation.setInterfaceParamId(paramId);
                        paramRelation.setSourceType(sourceType.toUpperCase(Locale.ROOT));
                        paramRelation.setTargetType(targetType.toUpperCase(Locale.ROOT));
                        interfaceObjectParamRelationMapper.insert(paramRelation);
                    });
                }
                // 有子集
                this.childrenInterfaceParam(interfaceId, paramId, paramType, paramReqVO.getChildren());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteInterface(String id) {

        // 接口信息
        interfaceMapper.deleteById(id);

        // 参数信息
        Wrapper<InterfaceParamEntity> deleteParam =
                Wrappers.<InterfaceParamEntity>query().lambda().eq(InterfaceParamEntity::getInterfaceId, id);
        interfaceParamMapper.delete(deleteParam);

        // 表关联信息
        Wrapper<InterfaceObjectParamRelationEntity> deleteRelation =
                Wrappers.<InterfaceObjectParamRelationEntity>query().lambda().eq(InterfaceObjectParamRelationEntity::getInterfaceId, id);
        interfaceObjectParamRelationMapper.delete(deleteRelation);

    }

    public InterfaceEntity findInterfaceByUrl(String url) {
        Wrapper<InterfaceEntity> wrapper = Wrappers.<InterfaceEntity>query()
                .lambda().eq(InterfaceEntity::getUrl, url);
        InterfaceEntity interfaceEntity = interfaceMapper.selectOne(wrapper);
        return interfaceEntity;
    }

    public InterfaceResVO findInterfaceById(String id) {
        InterfaceEntity interfaceEntity = interfaceMapper.selectById(id);
        InterfaceResVO interfaceVO = MasterDataMapper.INSTANCES.masterInterfaceEntity2VO(interfaceEntity);
        Map<String, List<InterfaceParamEntity>> maps = this.findInterfaceParamByInterfaceId(id);

        // 1 Params, 2 Body, 3 header, 4 cookie, 5 auth, 6 result
        interfaceVO.setParams(MasterDataMapper.INSTANCES.masterInterfaceParamEntitys2VO(maps.get("1")));
        interfaceVO.setHeader(MasterDataMapper.INSTANCES.masterInterfaceParamEntitys2VO(maps.get("3")));
        interfaceVO.setCookie(MasterDataMapper.INSTANCES.masterInterfaceParamEntitys2VO(maps.get("4")));
        interfaceVO.setAuth(MasterDataMapper.INSTANCES.masterInterfaceParamEntitys2VO(maps.get("5")));
        // result
        List<InterfaceParamEntity> interfaceParamEntityResult = maps.get("6");
        interfaceVO.setResult(this.createParamTree(interfaceParamEntityResult));

        // body
        List<InterfaceParamEntity> interfaceParamEntityBody = maps.get("2");
        interfaceVO.setBody(this.createParamTree(interfaceParamEntityBody));

        return interfaceVO;
    }

    public Map<String, List<InterfaceParamEntity>> findInterfaceParamByInterfaceId(String interfaceId) {
        Wrapper wrapper = Wrappers.<InterfaceParamEntity>query().lambda().eq(InterfaceParamEntity::getInterfaceId, interfaceId);
        List<InterfaceParamEntity> interfaceParamEntities = interfaceParamMapper.selectList(wrapper);
        Map<String, List<InterfaceParamEntity>> maps = Optional.ofNullable(interfaceParamEntities)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.groupingBy(InterfaceParamEntity::getParamType));

        return maps;
    }

    private InterfaceParamResVO createParamTree(List<InterfaceParamEntity> paramEntities) {

        if (CollectionUtils.isEmpty(paramEntities)) {
            return new InterfaceParamResVO();
        }

        InterfaceParamResVO paramRes = new InterfaceParamResVO();
        Map<String, InterfaceParamResVO> mapVO = new HashMap<>();

        // 只有一个根节点
        for (InterfaceParamEntity treeVo : paramEntities) {
            InterfaceParamResVO paramResVO = MasterDataMapper.INSTANCES.masterInterfaceParamEntity2VO(treeVo);
            // 添加关系信息
            if ("object".equals(paramResVO.getType())) {
                Wrapper<InterfaceObjectParamRelationEntity> relationWrapper =
                        Wrappers.<InterfaceObjectParamRelationEntity>query().lambda().eq(InterfaceObjectParamRelationEntity::getInterfaceParamId, paramResVO.getId());
                List<InterfaceObjectParamRelationEntity> relationEntities = interfaceObjectParamRelationMapper.selectList(relationWrapper);
                List<InterfaceObjectParamRelationResVO> relationResVOS = MasterDataMapper.INSTANCES.masterInterfaceObjectParamRelationEntitys2VO(relationEntities);
                paramResVO.setRelations(relationResVOS);
            }
            mapVO.put(treeVo.getId(), paramResVO);
            if (StringUtils.isEmpty(paramResVO.getParentId())) {
                paramRes = paramResVO;
            }
        }

        mapVO.forEach((s, masterInterfaceParamResVO) -> {
            if (masterInterfaceParamResVO.getParentId() != null) {
                InterfaceParamResVO parent = mapVO.get(masterInterfaceParamResVO.getParentId());
                if (parent != null) {
                    parent.getChildren().add(masterInterfaceParamResVO);
                }
            }
        });

        return paramRes;
    }

    public List<Map<String, Object>> sysTableColumnAll() {
        List<Map<String, Object>> tables = tableMapper.sysTable();
        List<Map<String, Object>> resultTable = Lists.newArrayList();
        Optional.ofNullable(tables).orElse(Collections.emptyList()).stream().forEach(table -> {
            Map<String, Object> mapTable = Maps.newHashMap();
            table.forEach((k, v) -> {
                mapTable.put(k.toLowerCase(), v);
            });
            // 表字段
            List<Map<String, Object>> tableColumns = tableMapper.sysTableColumn((String) table.get(""));
            List<Map<String, Object>> resultColumn = Lists.newArrayList();
            Optional.ofNullable(tableColumns).orElse(Collections.emptyList()).stream().forEach(tableColumn -> {
                Map<String, Object> map = Maps.newHashMap();
                tableColumn.forEach((kc, vc) -> {
                    map.put(kc.toLowerCase(), vc);
                });
                resultColumn.add(map);
            });
            mapTable.put("tableColumns", resultColumn);
            resultTable.add(mapTable);
        });
        return resultTable;
    }

    public List<Map<String, Object>> sysTable() {
        List<Map<String, Object>> tables = tableMapper.sysTable();
        List<Map<String, Object>> result = Lists.newArrayList();
        Optional.ofNullable(tables).orElse(Collections.emptyList()).stream().forEach(table -> {
            Map<String, Object> map = Maps.newHashMap();
            table.forEach((k, v) -> {
                map.put(k.toLowerCase(), v);
            });
            result.add(map);
        });
        return result;
    }

    public List<Map<String, Object>> sysTableColumn(String tableName) {
        List<Map<String, Object>> tableColumns = tableMapper.sysTableColumn(tableName);
        List<Map<String, Object>> result = Lists.newArrayList();
        Optional.ofNullable(tableColumns).orElse(Collections.emptyList()).stream().forEach(tableColumn -> {
            Map<String, Object> map = Maps.newHashMap();
            tableColumn.forEach((k, v) -> {
                map.put(k.toLowerCase(), v);
            });
            result.add(map);
        });
        return result;
    }
}
