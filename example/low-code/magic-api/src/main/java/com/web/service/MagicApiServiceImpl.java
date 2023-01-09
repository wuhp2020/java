package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.model.BaseDefinition;
import org.ssssssss.magicapi.core.model.DataType;
import org.ssssssss.magicapi.core.model.Group;
import org.ssssssss.magicapi.core.service.MagicResourceService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("MagicApiServiceImpl")
public class MagicApiServiceImpl implements IMagicApiService {

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
            groupSaveApi.setType("api");
            groupSaveApi.setParentId("0");
            groupSaveApi.setPath("/" + tableName.replaceAll("_", ""));
            magicResourceService.saveGroup(groupSaveApi);
        }
        List<ApiInfo> apiInfos = magicResourceService.listFiles(groupId);
        Map<String, ApiInfo> apiInfoMap = Optional.ofNullable(apiInfos).orElse(Collections.emptyList())
                .stream().collect(Collectors.toMap(ApiInfo::getPath, e->e));

        if (apiInfoMap.get("/findById") == null) {
            this.findById(groupId, tableName, columns);
        }
        if (apiInfoMap.get("/findByPage") == null) {
            this.findByPage(groupId, tableName, columns);
        }
        if (apiInfoMap.get("/deleteByIds") == null) {
            this.deleteByIds(groupId, tableName, columns);
        }
        if (apiInfoMap.get("/save") == null) {
            this.save(groupId, tableName, columns);
        }
        if (apiInfoMap.get("/updateById") == null) {
            this.updateById(groupId, tableName, columns);
        }
        if (apiInfoMap.get("/createOrUpdate") == null) {
            this.createOrUpdate(groupId, tableName, columns);
        }
        if (apiInfoMap.get("/findList") == null) {
            this.findList(groupId, tableName, columns);
        }
    }

    @Override
    public String type() {
        return "api";
    }

    private void findById(String groupId, String tableName, List<Map<String, Object>> columns) {
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
        Optional.ofNullable(columns).orElse(Collections.emptyList()).stream().forEach(column -> {
            String columnName = (String) column.get("COLUMN_NAME");
            if ("id".equals(columnName)) {
                String columnType = (String) column.get("COLUMN_TYPE");
                BaseDefinition baseDefinition = new BaseDefinition();
                baseDefinition.setName(columnName);
                baseDefinition.setDescription("主键");
                baseDefinition.setRequired(true);
                Map<DataType, Object> dataTypeObjectMap = this.convertColumnType(columnType);
                baseDefinition.setDataType(dataTypeObjectMap.keySet().stream().findFirst().get());
                baseDefinition.setValue(dataTypeObjectMap.get(dataTypeObjectMap.keySet().stream().findFirst().get()));
                children.add(baseDefinition);
            }
        });
        requestBodyDefinition.setChildren(children);
        apiInfo.setRequestBodyDefinition(requestBodyDefinition);

        BaseDefinition responseBodyDefinition = new BaseDefinition();
        responseBodyDefinition.setDataType(DataType.Object);
        responseBodyDefinition.setChildren(this.baseDefinitions(columns));
        apiInfo.setResponseBodyDefinition(responseBodyDefinition);
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(apiInfo);
    }

    private void findByPage(String groupId, String tableName, List<Map<String, Object>> columns) {
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

    private void deleteByIds(String groupId, String tableName, List<Map<String, Object>> columns) {
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
        Optional.ofNullable(columns).orElse(Collections.emptyList()).stream().forEach(column -> {
            String columnName = (String) column.get("COLUMN_NAME");
            if ("id".equals(columnName)) {
                String columnType = (String) column.get("COLUMN_TYPE");
                BaseDefinition baseDefinition = new BaseDefinition();
                baseDefinition.setName(columnName);
                baseDefinition.setDescription("主键");
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

    private void save(String groupId, String tableName, List<Map<String, Object>> columns) {
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

    private void updateById(String groupId, String tableName, List<Map<String, Object>> columns) {
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

    private void createOrUpdate(String groupId, String tableName, List<Map<String, Object>> columns) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setGroupId(groupId);
        apiInfo.setPath("/createOrUpdate");
        apiInfo.setUpdateBy("auto");
        apiInfo.setMethod("POST");
        apiInfo.setName("新增或修改");

        BaseDefinition requestBodyDefinition = new BaseDefinition();
        requestBodyDefinition.setRequired(true);
        requestBodyDefinition.setDataType(DataType.Object);
        StringBuilder sb = new StringBuilder();
        sb.append("import com.alibaba.fastjson.JSON;\n");
        sb.append("import com.web.util.SnowflakeIdWorker;\n");
        sb.append("var one = db.table('"+ tableName +"').where().eq('id', body.id).selectOne()\n");
        sb.append("if (one == null) {\n");
        sb.append("    db.table('"+ tableName +"').primary('id', SnowflakeIdWorker.getId()).save(JSON.toJSON(body))\n");
        sb.append("} else {\n");
        sb.append("    db.table('"+ tableName +"').primary('id').update(JSON.toJSON(body))\n");
        sb.append("}\n");
        sb.append("return \"ok\"");
        apiInfo.setScript(sb.toString());
        requestBodyDefinition.setChildren(this.baseDefinitions(columns));
        apiInfo.setRequestBodyDefinition(requestBodyDefinition);
        MagicResourceService magicResourceService = MagicConfiguration.getMagicResourceService();
        magicResourceService.saveFile(apiInfo);
    }

    private void findList(String groupId, String tableName, List<Map<String, Object>> columns) {
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
        columnType = columnType.split("\\(")[0].split(" ")[0];
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
