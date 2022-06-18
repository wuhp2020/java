package com.spring.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.modules.db.provider.ColumnMapperProvider;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/27
 * @ Desc   : 描述
 */
@Slf4j
@Component
public class CustomMapperProvider implements ColumnMapperProvider {

    @Override
    public String name() {
        // 当配置完成后
        // 可以在配置文件中全局配置magic-api.sql-column-case=custom
        // 也可以在代码中写 db.columnCase('custom').select(); 来使用
        return "custom";
    }

    @Override
    public String mapping(String columnName) {
        // 自定义逻辑
        return columnName;
    }

    @Override
    public String unmapping(String name) {
        // 自定义逻辑实现将转换后的列名还原为转换前的列名
        return name;
    }
}
