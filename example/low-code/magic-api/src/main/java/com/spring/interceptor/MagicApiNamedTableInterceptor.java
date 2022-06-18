package com.spring.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.modules.db.inteceptor.NamedTableInterceptor;
import org.ssssssss.magicapi.modules.db.model.SqlMode;
import org.ssssssss.magicapi.modules.db.table.NamedTable;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/26
 * @ Desc   : 描述
 */
@Slf4j
@Component
public class MagicApiNamedTableInterceptor implements NamedTableInterceptor {

    /**
     * 执行单表操作之前
     * @param sqlMode
     * @param namedTable
     */
    @Override
    public void preHandle(SqlMode sqlMode, NamedTable namedTable) {
        if (sqlMode == SqlMode.INSERT) {
            // 插入时id列用雪花算法
//            namedTable.column("id", 1);
        } else if (sqlMode == SqlMode.UPDATE) {

        } else if (sqlMode == SqlMode.DELETE) {

        } else if (sqlMode == SqlMode.UPDATE) {

        } else if (sqlMode == SqlMode.SELECT) {

        } else if (sqlMode == SqlMode.PAGE) {

        } else if (sqlMode == SqlMode.SELECT_ONE) {

        }
    }
}
