package com.web.service;

import com.web.vo.api.BaseDefinitionVO;
import com.web.vo.api.CreateApiVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/28
 * @ Desc   : 查
 */
@Service
@Slf4j
public class GetScriptService implements IScriptService {
    @Override
    public String script(CreateApiVO createApiVO) {
        StringBuilder sb = new StringBuilder("return db.select(\"\"\" ");
        StringBuilder sbField = new StringBuilder("select ");
        StringBuilder sbTable = new StringBuilder(" from ");
        StringBuilder sbWhere = new StringBuilder(" where ");
        BaseDefinitionVO responseBodyDefinition = createApiVO.getResponseBodyDefinition();
        sbTable.append(responseBodyDefinition.getName());
        sbTable.append(" ");
        sbTable.append(responseBodyDefinition.getName());
        this.children(responseBodyDefinition.getName(), responseBodyDefinition.getChildren(), sbTable, sbField);
        sbField.append(" 1 ");

        sbWhere.append(" 1 = 1 ");
        Optional.ofNullable(createApiVO.getParameters())
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
        sb.append(sbField.append(sbTable).append(sbWhere));
        sb.append(" \"\"\");");
        return sb.toString();
    }

    private void children(String tableName, List<BaseDefinitionVO> children, StringBuilder sbTable, StringBuilder sbField) {
        for (BaseDefinitionVO baseDefinitionVO: children) {
            if ("Object".equals(baseDefinitionVO.getDataType().toString())) {
                sbTable.append(" left join ");
                sbTable.append(baseDefinitionVO.getName());
                sbTable.append(" ");
                sbTable.append(baseDefinitionVO.getName());
                sbTable.append(" on ");
                sbTable.append(tableName);
                sbTable.append(".");
                sbTable.append(baseDefinitionVO.getName());
                sbTable.append("_id = ");
                sbTable.append(baseDefinitionVO.getName());
                sbTable.append(".");
                sbTable.append("id ");
                this.children(baseDefinitionVO.getName(), baseDefinitionVO.getChildren(), sbTable, sbField);
            } else {
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
    }

    @Override
    public String method() {
        return "GET";
    }
}
