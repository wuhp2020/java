package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.model.Group;
import org.ssssssss.magicapi.core.model.Parameter;
import org.ssssssss.magicapi.core.service.MagicResourceService;
import org.ssssssss.magicapi.function.model.FunctionInfo;

import java.util.*;
import java.util.stream.Collectors;

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
        List<FunctionInfo> functionInfos = magicResourceService.listFiles(groupId);
        Map<String, FunctionInfo> functionInfoMap = Optional.ofNullable(functionInfos).orElse(Collections.emptyList())
                .stream().collect(Collectors.toMap(FunctionInfo::getPath, e->e));

        if (functionInfoMap.get("/beforeFindById") == null) {
            this.beforeFindById(groupId);
        }
        if (functionInfoMap.get("/afterFindById") == null) {
            this.afterFindById(groupId);
        }
        if (functionInfoMap.get("/beforeFindByPage") == null) {
            this.beforeFindByPage(groupId);
        }
        if (functionInfoMap.get("/afterFindByPage") == null) {
            this.afterFindByPage(groupId);
        }
        if (functionInfoMap.get("/beforeDeleteByIds") == null) {
            this.beforeDeleteByIds(groupId);
        }
        if (functionInfoMap.get("/afterDeleteByIds") == null) {
            this.afterDeleteByIds(groupId);
        }
        if (functionInfoMap.get("/beforeSave") == null) {
            this.beforeSave(groupId);
        }
        if (functionInfoMap.get("/afterSave") == null) {
            this.afterSave(groupId);
        }
        if (functionInfoMap.get("/beforeUpdateById") == null) {
            this.beforeUpdateById(groupId);
        }
        if (functionInfoMap.get("/afterUpdateById") == null) {
            this.afterUpdateById(groupId);
        }
        if (functionInfoMap.get("/beforeCreateOrUpdate") == null) {
            this.beforeCreateOrUpdate(groupId);
        }
        if (functionInfoMap.get("/afterCreateOrUpdate") == null) {
            this.afterCreateOrUpdate(groupId);
        }
        if (functionInfoMap.get("/beforeFindList") == null) {
            this.beforeFindList(groupId);
        }
        if (functionInfoMap.get("/afterFindList") == null) {
            this.afterFindList(groupId);
        }
    }

    private void beforeFindById(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("查询详情前置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/beforeFindById");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("before");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void afterFindById(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("查询详情后置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/afterFindById");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("after");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void beforeFindByPage(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("查询分页前置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/beforeFindByPage");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("before");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void afterFindByPage(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("查询分页后置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/afterFindByPage");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("after");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void beforeDeleteByIds(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("删除前置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/beforeDeleteByIds");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("before");
        parameter1.setRequired(false);
        parameter1.setType("java.util.List");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void afterDeleteByIds(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("删除后置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/afterDeleteByIds");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("after");
        parameter1.setRequired(false);
        parameter1.setType("java.util.List");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void beforeSave(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("新增前置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/beforeSave");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("before");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void afterSave(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("新增后置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/afterSave");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("after");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void beforeUpdateById(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("修改前置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/beforeUpdateById");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("before");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void afterUpdateById(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("修改后置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/afterUpdateById");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("after");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void beforeCreateOrUpdate(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("新增或修改前置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/beforeCreateOrUpdate");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("before");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void afterCreateOrUpdate(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("新增或修改后置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/afterCreateOrUpdate");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("after");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void beforeFindList(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("查询集合前置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/beforeFindList");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("before");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    private void afterFindList(String groupId) {
        FunctionInfo functionInfo = new FunctionInfo();
        functionInfo.setGroupId(groupId);
        functionInfo.setName("查询集合后置");
        functionInfo.setUpdateBy("auto");
        functionInfo.setPath("/afterFindList");
        functionInfo.setReturnType("");
        List<Parameter> parameters = new LinkedList<>();
        Parameter parameter1 = new Parameter();
        parameter1.setName("after");
        parameter1.setRequired(false);
        parameter1.setType("java.util.Map");
        parameters.add(parameter1);
        functionInfo.setParameters(parameters);
        functionInfo.setScript("import log;\n");
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(functionInfo);
    }

    @Override
    public String type() {
        return "function";
    }
}
