<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.BaseMapper">


    <resultMap id="BaseResultMap" type="java.util.Map">
        <id column="id" jdbcType="BIGINT" property="id" />
        <result column="name" jdbcType="VARCHAR" property="name" />
        <association property="department_info" column="{id=id,}" javaType="map" select="selectDepartmentInfo" />
        <collection property="employees" column="{id=id,}" ofType="java.util.Map" javaType="java.util.List" select="selectEmployee"/>
    </resultMap>

    <resultMap id="selectDepartmentInfoMap" type="java.util.Map">
        <id column="id" jdbcType="BIGINT" property="id" />
        <result column="departmentid" jdbcType="VARCHAR" property="departmentid" />
        <result column="address" jdbcType="VARCHAR" property="address" />
        <result column="leader" jdbcType="VARCHAR" property="leader" />
        <result column="policy" jdbcType="VARCHAR" property="policy" />
    </resultMap>

    <resultMap id="selectEmployeeMap" type="java.util.Map">
        <id column="id" jdbcType="BIGINT" property="id" />
        <result column="name" jdbcType="VARCHAR" property="name" />
        <result column="age" jdbcType="BIGINT" property="age" />
        <result column="departmentid" jdbcType="VARCHAR" property="departmentid" />
        <result column="intotime" jdbcType="DATE" property="intotime" />
        <result column="salary" jdbcType="DOUBLE" property="salary" />
        <collection property="employee_works" column="{name=name,}" ofType="java.util.Map" javaType="java.util.List" select="selectEmployeeWork"/>
    </resultMap>

    <resultMap id="selectEmployeeWorkMap" type="java.util.Map">
        <id column="id" jdbcType="BIGINT" property="id" />
        <result column="name" jdbcType="VARCHAR" property="name" />
        <result column="intotime" jdbcType="DATE" property="intotime" />
    </resultMap>

    <select id="select" resultMap="BaseResultMap">
        select * from department
    </select>

    <select id="selectDepartmentInfo" resultMap="selectDepartmentInfoMap">
        select * from department_info where departmentid = #{id}
    </select>

    <select id="selectEmployee" resultMap="selectEmployeeMap">
        select * from employee where departmentid = #{id}
    </select>

    <select id="selectEmployeeWork" resultMap="selectEmployeeWorkMap">
        select * from employee_work where name = #{name}
    </select>

    <resultMap id="objectMap" type="java.util.Map">
        <collection property="department" column="x" ofType="java.util.Map" javaType="java.util.List" select="selectDepartment"/>
        <collection property="employee" column="x" ofType="java.util.Map" javaType="java.util.List" select="selectEmployee"/>
    </resultMap>

    <select id="selectObject" resultMap="objectMap">
        select 'x'
    </select>
</mapper>