package com.web.service;

import com.web.vo.InterfaceVO;
import com.web.vo.ModelTableColumnVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/31
 * @ Desc   : 描述
 */
@Service
@Slf4j
public class GetMyBatisXmlService implements IMyBatisXmlService {

    @Override
    public String xml(InterfaceVO createInterfaceVO) {
        String url = createInterfaceVO.getUrl().replaceAll("/", "");
        StringBuilder sbXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sbXML.append("<mapper namespace=\"com.BasicMapper\">\n\n");

        StringBuilder sbField = new StringBuilder("select ");
        StringBuilder sbTable = new StringBuilder(" from ");
        StringBuilder sbWhere = new StringBuilder(" where ");
        ModelTableColumnVO responseBodyDefinition = createInterfaceVO.getResult();

        StringBuilder sbResultMap = new StringBuilder("<resultMap id=\"BaseResultMap\" type=\"java.util.Map\">\n");

        sbTable.append(responseBodyDefinition.getName());
        sbTable.append(" ");
        sbTable.append(responseBodyDefinition.getName());
        this.childrenXML(url, true, responseBodyDefinition.getName(), responseBodyDefinition.getChildren(), sbTable, sbField, sbResultMap);
        sbResultMap.append("</resultMap>\n\n");
        sbField.append(" 1 ");

        sbWhere.append(" 1 = 1 ");
        Optional.ofNullable(createInterfaceVO.getParams())
                .orElse(Collections.emptyList())
                .stream()
                .forEach(parameterVO -> {
                    sbWhere.append(" and ");
                    sbWhere.append(parameterVO.getName().replaceFirst("_", "."));
                    sbWhere.append(" = ");
                    sbWhere.append("#{");
                    sbWhere.append(parameterVO.getName());
                    sbWhere.append("}");
                });

        StringBuilder sbSelect = new StringBuilder("<select id=\"select\" resultMap=\"BaseResultMap\">\n");
        sbSelect.append(sbField);
        sbSelect.append(sbTable);
        sbSelect.append(sbWhere);
        sbSelect.append("\n");
        sbSelect.append("</select>\n\n");

        sbXML.append(sbResultMap).append(sbSelect).append("</mapper>\n");


        return sbXML.toString();
    }

    private void childrenXML(String url, boolean isRoot, String tableName, List<ModelTableColumnVO> children,
                             StringBuilder sbTable, StringBuilder sbField, StringBuilder sbResultMap) {

        if (!isRoot) {
            sbResultMap.append("<collection property=\""+ tableName +"\" ofType=\"java.util.Map\" javaType=\"arraylist\">\n");
        }

        for (ModelTableColumnVO baseDefinitionVO: children) {
            if ("Object".equals(baseDefinitionVO.getType())) {
                sbTable.append(" left join ");
                sbTable.append(baseDefinitionVO.getName());
                sbTable.append(" ");
                sbTable.append(baseDefinitionVO.getName());
                sbTable.append(" on ");
                sbTable.append(baseDefinitionVO.getName());
                sbTable.append(".");
                sbTable.append(tableName);
                sbTable.append("_id = ");
                sbTable.append(tableName);
                sbTable.append(".");
                sbTable.append("id ");
                this.childrenXML(url, false, baseDefinitionVO.getName(), baseDefinitionVO.getChildren(), sbTable, sbField, sbResultMap);
            } else {
                if ("id".equals(baseDefinitionVO.getName())) {
                    sbResultMap.append("<id column=\""+ tableName + "_" + baseDefinitionVO.getName() +"\" jdbcType=\"BIGINT\" property=\"id\" />\n");
                } else {
                    sbResultMap.append("<result column=\""+ tableName + "_" + baseDefinitionVO.getName() +"\" jdbcType=\""+ baseDefinitionVO.getType() +"\" property=\""+ baseDefinitionVO.getName() +"\" />\n");
                }
                sbField.append(" ");
                sbField.append(tableName);
                sbField.append(".");
                sbField.append(baseDefinitionVO.getName());
                sbField.append(" as ");
                sbField.append(tableName);
                sbField.append("_");
                sbField.append(baseDefinitionVO.getName());
                sbField.append(", ");
            }
        }
        if (!isRoot) {
            sbResultMap.append("</collection>\n");
        }
    }

    @Override
    public String method() {
        return "GET";
    }
}
