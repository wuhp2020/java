package com.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/16
 * @ Desc   : 描述
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 获取当前数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("current datasource is {}", DynamicDataSourceContextHolder.getDataSourceKey());
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }
}
