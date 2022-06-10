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
 * @ Desc   : 增
 */
@Service
@Slf4j
public class PostScriptService implements IScriptService {

    @Override
    public String script(CreateApiVO createApiVO) {
        StringBuilder sbInsert = new StringBuilder("db.transaction(()=>{\n");
        BaseDefinitionVO responseBodyDefinition = createApiVO.getResponseBodyDefinition();
        this.children(responseBodyDefinition.getName(), responseBodyDefinition.getChildren(), sbInsert);
        sbInsert.append("\n});");
        return sbInsert.toString();
    }

    private void children(String tableName, List<BaseDefinitionVO> children, StringBuilder sbInsert) {
        StringBuilder sbInsertTemp = new StringBuilder(" db.table(");
        sbInsertTemp.append(tableName);
        sbInsertTemp.append(").insert(");
        sbInsert.append(sbInsertTemp);
        for (BaseDefinitionVO baseDefinitionVO: children) {
            if ("Object".equals(baseDefinitionVO.getDataType().toString())) {

                this.children(baseDefinitionVO.getName(), baseDefinitionVO.getChildren(), sbInsert);
            } else {

            }
        }
    }

    @Override
    public String method() {
        return "POST";
    }

}
