package com.web.service;

import com.google.common.collect.Maps;
import com.web.entity.InterfaceEntity;
import com.web.vo.InterfaceObjectParamRelationResVO;
import com.web.vo.InterfaceParamResVO;
import com.web.vo.InterfaceResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/1
 * @ Desc   : 描述
 */
@Service
@Slf4j
public class GetMyBatisDealService extends AbstractMyBatisDealService {

    @Override
    public Object invoke(HttpServletRequest request) throws Exception {

        InterfaceEntity interfaceEntity = interfaceService.findInterfaceByUrl(request.getRequestURI());
        if (interfaceEntity == null) {
            return "{\"code\": 404, \"msg\": \"暂无此服务, 请配置后调用\"}";
        }
        InterfaceResVO resVO = interfaceService.findInterfaceById(interfaceEntity.getId());

        // get请求参数从param中直接获取
        Map<String,Object> paramMap = Maps.newHashMap();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            paramMap.put(param, request.getParameter(param));
        }

        String xml = this.xml(resVO);
        log.debug("当前执行的xml:\n {}", xml);
        Object result = this.commonInvoke(xml, paramMap, "select");

        return "{\"code\": 200, \"msg\": \"ok\", \""+ resVO.getResult().getName() +"\": "+ gson.toJson(result) +"}";
    }

    @Override
    public String method() {
        return "GET";
    }

    @Override
    public String xml(InterfaceResVO resVO) {
        StringBuilder sbXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sbXML.append("<mapper namespace=\"com.BasicMapper\">\n\n");

        InterfaceParamResVO responseBodyDefinition = resVO.getResult();
        this.childrenXML("select", responseBodyDefinition, sbXML, resVO.getParams());
        sbXML.append("</mapper>\n");
        return sbXML.toString();
    }

    private void childrenXML(String resultMapId, InterfaceParamResVO result, StringBuilder sbXML, List<InterfaceParamResVO> params) {
        List<InterfaceParamResVO> children = result.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sbSQL = new StringBuilder();

            List<String> rs = children.stream().map(r -> r.getType()).collect(Collectors.toList())
                    .stream().filter(r -> !"object".equals(r)).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(rs)) {
                sb.append("<resultMap id=\""+ resultMapId +"\" type=\"java.util.Map\">\n");
                sbSQL.append("<select id=\""+ resultMapId +"\" resultMap=\""+ resultMapId +"\">\n select ");
                // 包含基本字段, 可能存在object
                String tableName = "";
                // 解决id顺序问题
                for (InterfaceParamResVO resVO: children) {
                    if (!"object".equals(resVO.getType())) {
                        if ("id".equals(resVO.getTableColumnName())) {
                            tableName = resVO.getTableName();
                            sbSQL.append(resVO.getTableColumnName() + " as " + resVO.getName() + ", ");
                            sb.append("<id column=\""+ resVO.getTableColumnName() +"\" jdbcType=\"BIGINT\" property=\""+ resVO.getName() +"\" />\n");
                        }
                    }
                }
                // 解决result顺序问题
                for (InterfaceParamResVO resVO: children) {
                    if (!"object".equals(resVO.getType())) {
                        if (!"id".equals(resVO.getTableColumnName())) {
                            tableName = resVO.getTableName();
                            sbSQL.append(resVO.getTableColumnName() + " as " + resVO.getName() + ", ");
                            sb.append("<result column=\""+ resVO.getTableColumnName() +"\" jdbcType=\""+ resVO.getType() +"\" property=\""+ resVO.getName() +"\" />\n");
                        }
                    }
                }

                // 参数预编译及往下传
                StringBuilder sbParam = new StringBuilder();
                StringBuilder paramInto = new StringBuilder();
                if (!CollectionUtils.isEmpty(params)) {
                    for (InterfaceParamResVO param: params) {
                        if (param.getName() != null) {
                            paramInto.append(param.getName());
                            paramInto.append("=");
                            paramInto.append(param.getName());
                            paramInto.append(",");
                            sbSQL.append("#{"+ param.getName() +"}");
                            sbSQL.append(" as ");
                            sbSQL.append(param.getName());
                            sbSQL.append(", ");
                        }
                        if (tableName.equals(param.getTableName())) {
                            sbParam.append("<if test=\""+ param.getName() +" != null and "+ param.getName() +" != ''\">\n");
                            sbParam.append(" and ");
                            sbParam.append(param.getTableColumnName());
                            sbParam.append(" = ");
                            sbParam.append(" #{"+ param.getName() +"} \n");
                            sbParam.append(" </if>\n");
                        }
                    }
                }

                // 解决collection顺序问题
                for (InterfaceParamResVO resVO: children) {
                    if ("object".equals(resVO.getType())) {
                        // 取关联关系
                        List<InterfaceObjectParamRelationResVO> relationResVOS = resVO.getRelations();
                        StringBuilder sbRelation = new StringBuilder();
                        for (InterfaceObjectParamRelationResVO relationResVO: relationResVOS) {
                            sbRelation.append(relationResVO.getSourceTableColumnName() +"="+relationResVO.getSourceTableColumnName() +",");
                        }
                        sb.append("<collection property=\""+ resVO.getName() +"\" column=\"{"+ sbRelation.toString() + paramInto.toString() +"}\" ofType=\"java.util.Map\" javaType=\"arraylist\" select=\""+ resVO.getName() +"\"/>\n");

                        this.childrenXML(resVO.getName(), resVO, sbXML, params);
                    }
                }

                sbSQL.append(" 1 from "+ tableName);
                sbSQL.append(" where 1 =1 \n");
                // 关联条件
                if (!CollectionUtils.isEmpty(result.getRelations())) {
                    for (InterfaceObjectParamRelationResVO relationResVO: result.getRelations()) {
                        sbSQL.append(" and ");
                        sbSQL.append(relationResVO.getTargetTableColumnName());
                        sbSQL.append(" = ");
                        sbSQL.append(" #{"+ relationResVO.getSourceTableColumnName() +"} \n");
                    }
                }

                sbSQL.append(sbParam);
                sbSQL.append("\n");
                sbSQL.append("</select>\n\n");
                sb.append("</resultMap>\n\n");
            } else {
                sb.append("<resultMap id=\""+ resultMapId +"\" type=\"java.util.Map\">\n");
                // 全是object
                for (InterfaceParamResVO resVO: children) {
                    sb.append("<collection property=\""+ resVO.getName() +"\" column=\"{x}\" ofType=\"java.util.Map\" javaType=\"arraylist\" select=\""+ resVO.getName() +"\"/>\n");
                    this.childrenXML(resVO.getName(), resVO, sbXML, params);
                }
            }

            sbXML.append(sb);
            sbXML.append(sbSQL);
        }
    }

}
