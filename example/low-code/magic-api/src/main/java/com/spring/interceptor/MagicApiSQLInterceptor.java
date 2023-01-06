package com.spring.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.context.RequestEntity;
import org.ssssssss.magicapi.modules.db.BoundSql;
import org.ssssssss.magicapi.modules.db.inteceptor.SQLInterceptor;
import java.util.Arrays;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/26
 * @ Desc   : 描述
 */
@Slf4j
@Component
public class MagicApiSQLInterceptor implements SQLInterceptor {

    @Override
    public void preHandle(BoundSql boundSql, RequestEntity requestEntity) {
//        log.info("要执行的SQL: " + boundSql.getSql());
        // 这里也可以通过boundSql的方法改写SQL和参数
//        log.info("要执行的SQL参数: " + Arrays.toString(boundSql.getParameters()));
    }
}
