package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.model.Group;
import org.ssssssss.magicapi.core.service.MagicResourceService;

import java.util.*;

@Slf4j
@Service("MagicApiFunctionServiceImpl")
public class MagicApiFunctionServiceImpl implements IMagicApiService {

    @Override
    public void autoCreate(Group group, String tableName, List<Map<String, Object>> columns) {
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        String groupId = "";
        if (group != null) {
            groupId = group.getId();
        } else {
            groupId = UUID.randomUUID().toString().replaceAll("-", "");
            // 接口
            Group groupSaveApi = new Group();
            groupSaveApi.setUpdateBy("auto");
            groupSaveApi.setId(groupId);
            groupSaveApi.setName(tableName.replaceAll("_", ""));
            groupSaveApi.setType("function");
            groupSaveApi.setParentId("0");
            groupSaveApi.setPath("/" + tableName.replaceAll("_", ""));
            magicResourceService.saveGroup(groupSaveApi);
        }
    }

    @Override
    public String type() {
        return "function";
    }
}
