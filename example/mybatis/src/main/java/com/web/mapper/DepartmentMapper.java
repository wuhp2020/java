package com.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DepartmentMapper {

    List<Map<String, Object>> selectDepartment();
    Map<String, Object> selectObject();
    void insertObject(Map<String, Object> paramMap);
}