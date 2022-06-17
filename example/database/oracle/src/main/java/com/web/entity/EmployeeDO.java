package com.web.entity;

import java.util.Date;

public class EmployeeDO {
    private Long id;

    private String name;

    private Long age;

    private String departmentid;

    private Date intotime;

    private Double salary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid == null ? null : departmentid.trim();
    }

    public Date getIntotime() {
        return intotime;
    }

    public void setIntotime(Date intotime) {
        this.intotime = intotime;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}