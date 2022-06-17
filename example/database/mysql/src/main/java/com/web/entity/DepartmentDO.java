package com.web.entity;

import lombok.Data;

import java.util.List;

//@Data
public class DepartmentDO {
    private Long id;

    private String name;

    private List<EmployeeDO> employee;
}