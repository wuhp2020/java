package com.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DepartmentMapper {

    Map<String, Object> select();
    Map<String, Object> selectObject();
    void insert(Map<String, Object> paramMap);
}