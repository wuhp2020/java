package com.web.service;

import com.web.mapper.DepartmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    public Map<String, Object> select() {
        return departmentMapper.select();
    }
    public Map<String, Object> selectObject() {
        return departmentMapper.selectObject();
    }
    public void insert(Map<String, Object> paramMap) {
        departmentMapper.insert(paramMap);
    }
}
