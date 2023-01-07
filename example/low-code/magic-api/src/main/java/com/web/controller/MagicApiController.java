package com.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.model.*;
import org.ssssssss.magicapi.core.service.MagicResourceService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/magicapi")
@Api(tags = "自动生成接口")
@Slf4j
public class MagicApiController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("reset")
    @ApiOperation(value = "重置资源")
    @Transactional(rollbackFor = Exception.class)
    public void reset() {
        this.deleteAutoGroupsFiles();
        this.autoCreateGroupsFiles();
    }

    /**
     * 删除自动创建的组、接口
     */
    private void deleteAutoGroupsFiles() {
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        Map<String, TreeNode<Group>> groupMap = magicResourceService.tree();
        Optional.ofNullable(groupMap).orElse(Collections.emptyMap()).forEach((k, v) -> {
            Optional.ofNullable(v).ifPresent(treeNode -> {
                Optional.ofNullable(treeNode.flat()).orElse(Collections.emptyList()).stream().forEach(group -> {
                    if (group != null && "auto".equals(group.getUpdateBy())) {
                        List<ApiInfo> apiInfos = magicResourceService.listFiles(group.getId());
                        List<ApiInfo> autoApiInfos = Optional.ofNullable(apiInfos).orElse(Collections.emptyList())
                                .stream().filter(apiInfo -> "auto".equals(apiInfo.getUpdateBy()))
                                .collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(apiInfos) && apiInfos.size() == autoApiInfos.size()) {
                            magicResourceService.delete(group.getId());
                        }
                        autoApiInfos.stream().forEach(autoApiInfo -> {
                            magicResourceService.delete(autoApiInfo.getId());
                        });
                    }
                });
            });
        });
    }

    /**
     * 自动创建组、接口
     */
    private void autoCreateGroupsFiles() {
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
                .collect(Collectors.toMap(Group::getPath, e->e));

        ColumnMapRowMapper tableRowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> tables = jdbcTemplate.query("select * from information_schema.TABLES where TABLE_SCHEMA=(select database())", tableRowMapper);

        Optional.ofNullable(tables).orElse(Collections.emptyList()).stream()
                .filter(table -> (!"magic_api".equals(table.get("TABLE_NAME")))).forEach(table -> {
            String tableNameReal = ((String) table.get("TABLE_NAME"));
            String tableName = ((String) table.get("TABLE_NAME")).replaceAll("_", "");
            String groupId = "";
            Group group = groupMap.get("/" + tableName);
            if (group != null) {
                groupId = group.getId();
            } else {
                groupId = UUID.randomUUID().toString().replaceAll("-", "");
                Group groupSave = new Group();
                groupSave.setUpdateBy("auto");
                groupSave.setId(groupId);
                groupSave.setName(tableName);
                groupSave.setType("api");
                groupSave.setParentId("0");
                groupSave.setPath("/" + tableName);
                magicResourceService.saveGroup(groupSave);
            }

            ColumnMapRowMapper columnRowMapper = new ColumnMapRowMapper();
            List<Map<String, Object>> columnMap = jdbcTemplate.query("select * from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME='" +tableNameReal+ "'", columnRowMapper);

            List<ApiInfo> apiInfos = magicResourceService.listFiles(groupId);
            Map<String, ApiInfo> apiInfoMap = Optional.ofNullable(apiInfos).orElse(Collections.emptyList())
                    .stream().collect(Collectors.toMap(ApiInfo::getPath, e->e));

            if (apiInfoMap.get("/findById") == null) {
                this.autoCreateFindById(groupId, tableNameReal, columnMap);
            }
            if (apiInfoMap.get("/findByPage") == null) {
                this.autoCreateFindByPage(groupId, tableNameReal, columnMap);
            }
            if (apiInfoMap.get("/deleteByIds") == null) {
                this.autoCreateDeleteByIds(groupId, tableNameReal, columnMap);
            }
            if (apiInfoMap.get("/save") == null) {
                this.autoCreateSave(groupId, tableNameReal, columnMap);
            }
            if (apiInfoMap.get("/updateById") == null) {
                this.autoCreateUpdateById(groupId, tableNameReal, columnMap);
            }
            if (apiInfoMap.get("/findList") == null) {
                this.autoCreateFindList(groupId, tableNameReal, columnMap);
            }
        });
    }

    private void autoCreateFindById(String groupId, String tableName, List<Map<String, Object>> columns) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setGroupId(groupId);
        apiInfo.setPath("/findById");
        apiInfo.setUpdateBy("auto");
        apiInfo.setMethod("POST");
        apiInfo.setName("查询详情");
        apiInfo.setScript("return db.table('"+ tableName +"').where().eq('id', body.id).selectOne()");
        BaseDefinition requestBodyDefinition = new BaseDefinition();
        requestBodyDefinition.setRequired(true);
        requestBodyDefinition.setDataType(DataType.Object);
        ArrayList<BaseDefinition> children = new ArrayList<>();
        BaseDefinition baseDefinition = new BaseDefinition();
        baseDefinition.setName("id");
        baseDefinition.setRequired(true);
        baseDefinition.setDataType(DataType.String);
        baseDefinition.setDescription("主键");
        baseDefinition.setValue("1");
        children.add(baseDefinition);
        requestBodyDefinition.setChildren(children);
        apiInfo.setRequestBodyDefinition(requestBodyDefinition);

        BaseDefinition responseBodyDefinition = new BaseDefinition();
        responseBodyDefinition.setDataType(DataType.Object);
        responseBodyDefinition.setChildren(this.baseDefinitions(columns));
        apiInfo.setResponseBodyDefinition(responseBodyDefinition);
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(apiInfo);
    }

    private void autoCreateFindByPage(String groupId, String tableName, List<Map<String, Object>> columns) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setGroupId(groupId);
        apiInfo.setPath("/findByPage");
        apiInfo.setUpdateBy("auto");
        apiInfo.setMethod("POST");
        apiInfo.setName("查询分页");

        BaseDefinition requestBodyDefinition = new BaseDefinition();
        requestBodyDefinition.setRequired(true);
        requestBodyDefinition.setDataType(DataType.Object);
        ArrayList<BaseDefinition> children = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("var sql = \"\"\"\n");
        sb.append("select * from ");
        sb.append(tableName);
        sb.append("\n<where>\n");
        Optional.ofNullable(columns).orElse(Collections.emptyList()).stream().forEach(column -> {
            String columnName = (String) column.get("COLUMN_NAME");
            String columnType = (String) column.get("COLUMN_TYPE");
            String comment = (String) column.get("COLUMN_COMMENT");
            sb.append("  <if test=\"body."+ columnName +" != null and body."+ columnName +" != ''\">\n");
            sb.append("    and "+ columnName +" = #{body."+ columnName +"}\n");
            sb.append("  </if>\n");

            BaseDefinition baseDefinition = new BaseDefinition();
            baseDefinition.setName(columnName);
            baseDefinition.setRequired(false);
            baseDefinition.setDescription(comment);
            Map<DataType, Object> dataTypeObjectMap = this.convertColumnType(columnType);
            baseDefinition.setDataType(dataTypeObjectMap.keySet().stream().findFirst().get());
            baseDefinition.setValue(dataTypeObjectMap.get(dataTypeObjectMap.keySet().stream().findFirst().get()));
            children.add(baseDefinition);
        });
        sb.append("</where>\n");
        sb.append("\"\"\"\n");
        sb.append("return db.page(sql, body.pageSize, body.pageNum * body.pageSize)");
        apiInfo.setScript(sb.toString());

        BaseDefinition baseDefinitionPageNum = new BaseDefinition();
        baseDefinitionPageNum.setName("pageNum");
        baseDefinitionPageNum.setRequired(true);
        baseDefinitionPageNum.setDataType(DataType.Integer);
        baseDefinitionPageNum.setValue(1);
        children.add(baseDefinitionPageNum);

        BaseDefinition baseDefinitionPageSize = new BaseDefinition();
        baseDefinitionPageSize.setName("pageSize");
        baseDefinitionPageSize.setRequired(true);
        baseDefinitionPageSize.setDataType(DataType.Integer);
        baseDefinitionPageSize.setValue(10);
        children.add(baseDefinitionPageSize);

        requestBodyDefinition.setChildren(children);
        apiInfo.setRequestBodyDefinition(requestBodyDefinition);

        BaseDefinition responseBodyDefinition = new BaseDefinition();
        responseBodyDefinition.setDataType(DataType.Array);
        responseBodyDefinition.setChildren(this.baseDefinitions(columns));
        apiInfo.setResponseBodyDefinition(responseBodyDefinition);

        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(apiInfo);
    }

    private void autoCreateDeleteByIds(String groupId, String tableName, List<Map<String, Object>> columns) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setGroupId(groupId);
        apiInfo.setPath("/deleteByIds");
        apiInfo.setUpdateBy("auto");
        apiInfo.setMethod("POST");
        apiInfo.setName("删除");
        StringBuilder sb = new StringBuilder();
        sb.append("db.transaction(()=>{\n");
        sb.append("    for(index,item in body) {\n");
        sb.append("        db.table('"+ tableName +"').where().eq('id', item).delete()\n");
        sb.append("    }\n");
        sb.append("});\n");
        sb.append("return \"ok\"");
        apiInfo.setScript(sb.toString());
        BaseDefinition requestBodyDefinition = new BaseDefinition();
        requestBodyDefinition.setRequired(true);
        requestBodyDefinition.setDataType(DataType.Array);
        ArrayList<BaseDefinition> children = new ArrayList<>();
        BaseDefinition baseDefinition = new BaseDefinition();
        baseDefinition.setRequired(true);
        baseDefinition.setDataType(DataType.String);
        baseDefinition.setValue("1");
        baseDefinition.setDescription("主键");
        children.add(baseDefinition);
        requestBodyDefinition.setChildren(children);
        apiInfo.setRequestBodyDefinition(requestBodyDefinition);
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(apiInfo);
    }

    private void autoCreateSave(String groupId, String tableName, List<Map<String, Object>> columns) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setGroupId(groupId);
        apiInfo.setPath("/save");
        apiInfo.setUpdateBy("auto");
        apiInfo.setMethod("POST");
        apiInfo.setName("新增");

        BaseDefinition requestBodyDefinition = new BaseDefinition();
        requestBodyDefinition.setRequired(true);
        requestBodyDefinition.setDataType(DataType.Object);
        StringBuilder sb = new StringBuilder();
        sb.append("import com.alibaba.fastjson.JSON;\n");
        sb.append("import com.web.util.SnowflakeIdWorker;\n");
        sb.append("db.table('"+ tableName +"').primary('id', SnowflakeIdWorker.getId()).save(JSON.toJSON(body))\n");
        sb.append("return \"ok\"");
        apiInfo.setScript(sb.toString());

        ArrayList<BaseDefinition> children = new ArrayList<>();
        Optional.ofNullable(columns).orElse(Collections.emptyList()).stream().forEach(column -> {
            String columnName = (String) column.get("COLUMN_NAME");
            if (!"id".equals(columnName)) {
                String columnType = (String) column.get("COLUMN_TYPE");
                String comment = (String) column.get("COLUMN_COMMENT");
                BaseDefinition baseDefinition = new BaseDefinition();
                baseDefinition.setName(columnName);
                baseDefinition.setDescription(comment);
                baseDefinition.setRequired(false);
                Map<DataType, Object> dataTypeObjectMap = this.convertColumnType(columnType);
                baseDefinition.setDataType(dataTypeObjectMap.keySet().stream().findFirst().get());
                baseDefinition.setValue(dataTypeObjectMap.get(dataTypeObjectMap.keySet().stream().findFirst().get()));
                children.add(baseDefinition);
            }
        });
        requestBodyDefinition.setChildren(children);
        apiInfo.setRequestBodyDefinition(requestBodyDefinition);
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(apiInfo);
    }

    private void autoCreateUpdateById(String groupId, String tableName, List<Map<String, Object>> columns) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setGroupId(groupId);
        apiInfo.setPath("/updateById");
        apiInfo.setUpdateBy("auto");
        apiInfo.setMethod("POST");
        apiInfo.setName("修改");

        BaseDefinition requestBodyDefinition = new BaseDefinition();
        requestBodyDefinition.setRequired(true);
        requestBodyDefinition.setDataType(DataType.Object);
        StringBuilder sb = new StringBuilder();
        sb.append("import com.alibaba.fastjson.JSON;\n");
        sb.append("db.table('"+ tableName +"').primary('id').update(JSON.toJSON(body))\n");
        sb.append("return \"ok\"");
        apiInfo.setScript(sb.toString());

        ArrayList<BaseDefinition> children = new ArrayList<>();
        Optional.ofNullable(columns).orElse(Collections.emptyList()).stream().forEach(column -> {
            String columnName = (String) column.get("COLUMN_NAME");
            String columnType = (String) column.get("COLUMN_TYPE");
            String comment = (String) column.get("COLUMN_COMMENT");
            BaseDefinition baseDefinition = new BaseDefinition();
            baseDefinition.setName(columnName);
            if ("id".equals(columnName)) {
                baseDefinition.setRequired(true);
            } else {
                baseDefinition.setRequired(false);
            }
            baseDefinition.setDescription(comment);
            Map<DataType, Object> dataTypeObjectMap = this.convertColumnType(columnType);
            baseDefinition.setDataType(dataTypeObjectMap.keySet().stream().findFirst().get());
            baseDefinition.setValue(dataTypeObjectMap.get(dataTypeObjectMap.keySet().stream().findFirst().get()));
            children.add(baseDefinition);
        });
        requestBodyDefinition.setChildren(children);
        apiInfo.setRequestBodyDefinition(requestBodyDefinition);
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(apiInfo);
    }

    private void autoCreateFindList(String groupId, String tableName, List<Map<String, Object>> columns) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setGroupId(groupId);
        apiInfo.setPath("/findList");
        apiInfo.setUpdateBy("auto");
        apiInfo.setMethod("POST");
        apiInfo.setName("查询集合");

        BaseDefinition requestBodyDefinition = new BaseDefinition();
        requestBodyDefinition.setRequired(false);
        requestBodyDefinition.setDataType(DataType.Object);
        ArrayList<BaseDefinition> children = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("var sql = \"\"\"\n");
        sb.append("select * from ");
        sb.append(tableName);
        sb.append("\n<where>\n");
        Optional.ofNullable(columns).orElse(Collections.emptyList()).stream().forEach(column -> {
            String columnName = (String) column.get("COLUMN_NAME");
            String columnType = (String) column.get("COLUMN_TYPE");
            String comment = (String) column.get("COLUMN_COMMENT");
            sb.append("  <if test=\"body."+ columnName +" != null and body."+ columnName +" != ''\">\n");
            sb.append("    and "+ columnName +" = #{body."+ columnName +"}\n");
            sb.append("  </if>\n");

            BaseDefinition baseDefinition = new BaseDefinition();
            baseDefinition.setName(columnName);
            baseDefinition.setRequired(false);
            baseDefinition.setDescription(comment);
            Map<DataType, Object> dataTypeObjectMap = this.convertColumnType(columnType);
            baseDefinition.setDataType(dataTypeObjectMap.keySet().stream().findFirst().get());
            baseDefinition.setValue(dataTypeObjectMap.get(dataTypeObjectMap.keySet().stream().findFirst().get()));
            children.add(baseDefinition);
        });
        sb.append("</where>\n");
        sb.append("\"\"\"\n");
        sb.append("return db.select(sql)");
        apiInfo.setScript(sb.toString());

        requestBodyDefinition.setChildren(children);
        apiInfo.setRequestBodyDefinition(requestBodyDefinition);

        BaseDefinition responseBodyDefinition = new BaseDefinition();
        responseBodyDefinition.setDataType(DataType.Array);
        responseBodyDefinition.setChildren(this.baseDefinitions(columns));
        apiInfo.setResponseBodyDefinition(responseBodyDefinition);

        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(apiInfo);
    }

    private ArrayList<BaseDefinition> baseDefinitions(List<Map<String, Object>> columns) {
        ArrayList<BaseDefinition> baseDefinitions = new ArrayList<>();
        Optional.ofNullable(columns).orElse(Collections.emptyList()).stream().forEach(column -> {
            String columnName = (String) column.get("COLUMN_NAME");
            String columnType = (String) column.get("COLUMN_TYPE");
            String comment = (String) column.get("COLUMN_COMMENT");
            BaseDefinition baseDefinition = new BaseDefinition();
            baseDefinition.setName(columnName);
            baseDefinition.setRequired(false);
            Map<DataType, Object> dataTypeObjectMap = this.convertColumnType(columnType);
            baseDefinition.setDataType(dataTypeObjectMap.keySet().stream().findFirst().get());
            baseDefinition.setValue(dataTypeObjectMap.get(dataTypeObjectMap.keySet().stream().findFirst().get()));
            baseDefinition.setDescription(comment);
            baseDefinitions.add(baseDefinition);
        });

        return baseDefinitions;
    }

    private Map<DataType, Object> convertColumnType(String columnType) {
        columnType = columnType.split("\\(")[0];
        Map<DataType, Object> result = new HashMap<>();
        if ("varchar".equals(columnType)) {
            result.put(DataType.String, "1");
        } else if ("bigint".equals(columnType)) {
            result.put(DataType.Long, 1L);
        } else if ("decimal".equals(columnType)) {
            result.put(DataType.Double, 1.00D);
        } else if ("int".equals(columnType)) {
            result.put(DataType.Integer, 1);
        } else if ("tinyint".equals(columnType)) {
            result.put(DataType.Boolean, true);
        } else if ("float".equals(columnType)) {
            result.put(DataType.Float, 1.0F);
        } else if ("double".equals(columnType)) {
            result.put(DataType.Double, 1.00D);
        } else if ("datetime".equals(columnType)) {
            result.put(DataType.String, "2020-10-18 10:11:32");
        } else if ("date".equals(columnType)) {
            result.put(DataType.String, "2020-10-18");
        } else if ("timestamp".equals(columnType)) {
            result.put(DataType.Date, new Date());
        } else if ("time".equals(columnType)) {
            result.put(DataType.Date, new Date());
        } else if ("char".equals(columnType)) {
            result.put(DataType.String, "1");
        } else if ("text".equals(columnType)) {
            result.put(DataType.String, "1");
        }
        return result;
    }

}
