package com.web.mapper;

import com.web.entity.EmployeeDO;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EmployeeDO record);

    int insertSelective(EmployeeDO record);

    EmployeeDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EmployeeDO record);

    int updateByPrimaryKey(EmployeeDO record);
}