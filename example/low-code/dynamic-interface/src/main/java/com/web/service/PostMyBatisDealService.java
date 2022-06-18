package com.web.service;

import com.web.entity.InterfaceEntity;
import com.web.vo.InterfaceObjectParamRelationResVO;
import com.web.vo.InterfaceParamResVO;
import com.web.vo.InterfaceResVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/1
 * @ Desc   : 描述
 */
@Service
@Slf4j
public class PostMyBatisDealService extends AbstractMyBatisDealService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object invoke(HttpServletRequest request) throws Exception {

        InterfaceEntity interfaceEntity = interfaceService.findInterfaceByUrl(request.getRequestURI());
        if (interfaceEntity == null) {
            return "{\"code\": 404, \"msg\": \"暂无此服务, 请配置后调用\"}";
        }
        InterfaceResVO resVO = interfaceService.findInterfaceById(interfaceEntity.getId());

        // 入参
        StringBuilder data = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while (null != (line = reader.readLine())) {
            data.append(line);
        }
        Map<String, Object> paramMap = gson.fromJson(data.toString(), Map.class);

        String xml = this.xml(resVO);

        // 设置参数: 雪花算法ID, 关联关系的数据
        this.param(paramMap, resVO.getBody(), null);

        log.debug("当前执行的xml:\n {}", xml);
        this.commonInvoke(xml, paramMap, "insert");

        return "{\"code\": 200, \"msg\": \"ok\"}";
    }

    /**
     * 参数处理
     * @param paramMap
     * @param resVO
     */
    private void param(Map<String, Object> paramMap, InterfaceParamResVO resVO, Map<String, Object> relationMap) {
        List<InterfaceParamResVO> children = resVO.getChildren();
        if (!CollectionUtils.isEmpty(children)) {

            List<String> rs = children.stream().map(r -> r.getType()).collect(Collectors.toList())
                    .stream().filter(r -> !"object".equals(r)).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(rs) && !CollectionUtils.isEmpty(paramMap)) {
                // 添加ID
                String id = SnowflakeIdWorker.getId();
                if (!paramMap.keySet().contains("id")) {
                    paramMap.put("id", id);
                }
                // 关联数据添加
                Map<String, String> isHave = children.stream()
                        .collect(Collectors.toMap(e -> e.getTableName() +"_"+e.getTableColumnName(), InterfaceParamResVO::getName));
                Optional.ofNullable(relationMap).orElse(Collections.emptyMap()).forEach((k, v) -> {
                    if (!paramMap.containsKey(isHave.get(k))) {
                        paramMap.put(k, v);
                    }
                });

                Map<String, String> paramNames = children.stream()
                        .collect(Collectors.toMap(InterfaceParamResVO::getTableColumnName, InterfaceParamResVO::getName));
                for (InterfaceParamResVO paramResVO: children) {
                    if ("object".equals(paramResVO.getType())) {
                        // 组装关联数据
                        Map<String, Object> subRelationMap = new HashMap<>();
                        List<InterfaceObjectParamRelationResVO> relations = paramResVO.getRelations();
                        Optional.ofNullable(relations).orElse(Collections.emptyList())
                                .stream().forEach(relation -> {
                                    if ("id".equals(relation.getSourceTableColumnName())) {
                                        subRelationMap.put(relation.getTargetTableName() +"_"+ relation.getTargetTableColumnName(), id);
                                    } else {
                                        Object objRelation = paramMap.get(paramNames.get(relation.getSourceTableColumnName()));
                                        subRelationMap.put(relation.getTargetTableName() +"_"+ relation.getTargetTableColumnName(), objRelation);
                                    }
                                });
                        if (paramMap.get(paramResVO.getName()) != null) {
                            this.childrenParam((List<Map<String, Object>>) paramMap.get(paramResVO.getName()), paramResVO, subRelationMap);
                        }
                    }
                }
            } else {

            }
        }
    }

    private void childrenParam(List<Map<String, Object>> subParam, InterfaceParamResVO resVO, Map<String, Object> relationMap) {
        if(!CollectionUtils.isEmpty(subParam)) {
            subParam.stream().forEach(param -> {
                this.param(param, resVO, relationMap);
            });
        }
    }

    @Override
    public String xml(InterfaceResVO resVO) {
        StringBuilder sbXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sbXML.append("<mapper namespace=\"com.BasicMapper\">\n\n");

        InterfaceParamResVO responseBodyDefinition = resVO.getBody();
        sbXML.append("<insert id=\"insert\" parameterType=\"map\">\n");
        this.childrenXML(responseBodyDefinition, sbXML, null);
        sbXML.append("</insert>\n");
        sbXML.append("</mapper>\n");
        return sbXML.toString();
    }

    private void childrenXML(InterfaceParamResVO paramResVO, StringBuilder sbXML, String objectName) {
        List<InterfaceParamResVO> children = paramResVO.getChildren();
        if (!CollectionUtils.isEmpty(children)) {

            List<String> rs = children.stream().map(r -> r.getType()).collect(Collectors.toList())
                    .stream().filter(r -> !"object".equals(r)).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(rs)) {
                String tableName = "";

                StringBuilder columnName = new StringBuilder();
                StringBuilder columnValue = new StringBuilder();
                for (InterfaceParamResVO resVO : children) {
                    if (!"object".equals(resVO.getType())) {
                        tableName = resVO.getTableName();
                        break;
                    }
                }

                sbXML.append(" insert into ");
                sbXML.append(tableName);
                sbXML.append("\n");
                columnName.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
                columnValue.append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
                for (InterfaceParamResVO resVO : children) {
                    if (!"object".equals(resVO.getType())) {
                        columnName.append("<if test=\""+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + resVO.getName() +" != null\">");
                        columnName.append(resVO.getTableColumnName());
                        columnName.append(",");
                        columnName.append("</if>\n");

                        columnValue.append("<if test=\""+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + resVO.getName() +" != null\">");
                        columnValue.append("#{"+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + resVO.getName() +",jdbcType="+ resVO.getType() +"},");
                        columnValue.append("</if>\n");
                    }
                }

                // 关联数据
                List<String> childrenNames = children.stream().map(InterfaceParamResVO::getTableColumnName).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(paramResVO.getRelations())) {
                    paramResVO.getRelations().stream().forEach(relationResVO -> {
                        // 防重复, 字段拼接表名
                        columnName.append("<if test=\""+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + relationResVO.getTargetTableName() +"_"+ relationResVO.getTargetTableColumnName() +" != null\">");
                        columnName.append(relationResVO.getTargetTableColumnName());
                        columnName.append(",");
                        columnName.append("</if>\n");

                        columnValue.append("<if test=\""+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + relationResVO.getTargetTableName() +"_"+ relationResVO.getTargetTableColumnName() +" != null\">");
                        columnValue.append("#{"+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + relationResVO.getTargetTableName() +"_"+ relationResVO.getTargetTableColumnName() +",jdbcType="+ relationResVO.getTargetType() +"},");
                        columnValue.append("</if>\n");
                    });
                }

                columnName.append("</trim>\n");
                columnValue.append("</trim>;\n\n");

                sbXML.append(columnName);
                sbXML.append(columnValue);

                for (InterfaceParamResVO resVO : children) {
                    if ("object".equals(resVO.getType())) {
                        sbXML.append("<if test=\""+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + resVO.getName() +" != null and "+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + resVO.getName() +".size>0\" >\n");
                        sbXML.append("<foreach collection=\""+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + resVO.getName() +"\" item=\""+ resVO.getName() +"item\" index=\"index\">\n");
                        this.childrenXML(resVO, sbXML, resVO.getName() +"item");
                        sbXML.append("</foreach>\n");
                        sbXML.append("</if>\n");
                    }
                }
            } else {
                for (InterfaceParamResVO resVO : children) {
                    if ("object".equals(resVO.getType())) {
                        sbXML.append("<if test=\""+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + resVO.getName() +" != null and "+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + resVO.getName() +".size>0\" >\n");
                        sbXML.append("<foreach collection=\""+ (StringUtils.isEmpty(objectName)? "": objectName + ".") + resVO.getName() +"\" item=\""+ resVO.getName() +"item\" index=\"index\">\n");
                        this.childrenXML(resVO, sbXML, resVO.getName() +"item");
                        sbXML.append("</foreach>\n");
                        sbXML.append("</if>\n");
                    }
                }
            }
        }
    }

    @Override
    public String method() {
        return "POST";
    }
}
