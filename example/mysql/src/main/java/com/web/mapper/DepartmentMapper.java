package com.web.mapper;

import com.web.entity.DepartmentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    List<DepartmentDO> selectAll();
}