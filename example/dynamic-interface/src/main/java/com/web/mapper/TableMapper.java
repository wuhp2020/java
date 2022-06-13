package com.web.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableMapper {

    @Select("select * from information_schema.TABLES where TABLE_SCHEMA=(select database())")
    List<Map<String, Object>> sysTable();

    @Select("select * from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME=#{tableName}")
    List<Map<String, Object>> sysTableColumn(String tableName);

}

