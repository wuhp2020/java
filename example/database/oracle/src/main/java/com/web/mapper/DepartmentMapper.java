package com.web.mapper;

import com.web.entity.DepartmentDO;

public interface DepartmentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DepartmentDO record);

    int insertSelective(DepartmentDO record);

    DepartmentDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DepartmentDO record);

    int updateByPrimaryKey(DepartmentDO record);
}