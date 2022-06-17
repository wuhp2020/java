package com.spring.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment evn;

    @Bean
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 设置原始数据源
        Map<Object, Object> dataSourcesMap = new HashMap<>();
        DruidDataSource privateDataSource = this.createDataSource("private");
        DruidDataSource defaultDataSource = this.createDataSource("default");
        dataSourcesMap.put("private", privateDataSource);
        dataSourcesMap.put("default", defaultDataSource);
        dynamicDataSource.setTargetDataSources(dataSourcesMap);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        return dynamicDataSource;
    }

    /**
     * 创建数据源对象
     * @param dbType 数据库类型
     * @return data source
     */
    private DruidDataSource createDataSource(String dbType) {
        //如果不指定数据库类型, 则使用默认数据库连接
        String dbName = dbType.trim().isEmpty() ? "default" : dbType.trim();
        DruidDataSource dataSource = new DruidDataSource();
        String prefix = "db." + dbName +".";
        String dbUrl = evn.getProperty( prefix + "url");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(evn.getProperty( prefix + "username"));
        dataSource.setPassword(evn.getProperty( prefix + "password"));
        dataSource.setDriverClassName(evn.getProperty( prefix + "driverClassName"));
        return dataSource;
    }
}
