<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.web.mapper.DepartmentMapper">

    <insert id="insert" parameterType="map">
        insert into department
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="id != null">
                id,
            </if>
            <if test="name != null">
                name,
            </if>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <if test="id != null">
                #{id,jdbcType=BIGINT},
            </if>
            <if test="name != null">
                #{name,jdbcType=VARCHAR},
            </if>
        </trim>;

        <if test="department_info != null">
            insert into department_info
            <trim prefix="(" suffix=")" suffixOverrides=",">
                <if test="department_info.id != null">
                    id,
                </if>
                <if test="department_info.departmentid != null">
                    departmentid,
                </if>
                <if test="department_info.address != null">
                    address,
                </if>
                <if test="department_info.leader != null">
                    leader,
                </if>
                <if test="department_info.policy != null">
                    policy,
                </if>
            </trim>
            <trim prefix="values (" suffix=")" suffixOverrides=",">
                <if test="department_info.id != null">
                    #{department_info.id,jdbcType=BIGINT},
                </if>
                <if test="department_info.departmentid != null">
                    #{department_info.departmentid,jdbcType=VARCHAR},
                </if>
                <if test="department_info.address != null">
                    #{department_info.address,jdbcType=VARCHAR},
                </if>
                <if test="department_info.leader != null">
                    #{department_info.leader,jdbcType=VARCHAR},
                </if>
                <if test="department_info.policy != null">
                    #{department_info.policy,jdbcType=VARCHAR},
                </if>
            </trim>;
        </if>

        <if test="employees != null and employees.size>0" >
            <foreach collection="employees" item="employee" index="index">
                insert into employee
                <trim prefix="(" suffix=")" suffixOverrides=",">
                    <if test="employee.id != null">
                        id,
                    </if>
                    <if test="employee.name != null">
                        name,
                    </if>
                    <if test="employee.age != null">
                        age,
                    </if>
                    <if test="employee.departmentid != null">
                        departmentid,
                    </if>
                    <if test="employee.intotime != null">
                        intotime,
                    </if>
                    <if test="employee.salary != null">
                        salary,
                    </if>
                </trim>
                <trim prefix="values (" suffix=")" suffixOverrides=",">
                    <if test="employee.id != null">
                        #{employee.id,jdbcType=BIGINT},
                    </if>
                    <if test="employee.name != null">
                        #{employee.name,jdbcType=VARCHAR},
                    </if>
                    <if test="employee.age != null">
                        #{employee.age,jdbcType=BIGINT},
                    </if>
                    <if test="employee.departmentid != null">
                        #{employee.departmentid,jdbcType=VARCHAR},
                    </if>
                    <if test="employee.intotime != null">
                        #{employee.intotime,jdbcType=DATE},
                    </if>
                    <if test="employee.salary != null">
                        #{employee.salary,jdbcType=DOUBLE},
                    </if>
                </trim>;

                <if test="employee.employee_works != null and employee.employee_works.size>0" >
                    <foreach collection="employee.employee_works" item="employee_work" index="index">
                        insert into employee_work
                        <trim prefix="(" suffix=")" suffixOverrides=",">
                            <if test="employee_work.id != null">
                                id,
                            </if>
                            <if test="employee_work.name != null">
                                name,
                            </if>
                            <if test="employee_work.intotime != null">
                                intotime,
                            </if>
                        </trim>
                        <trim prefix="values (" suffix=")" suffixOverrides=",">
                            <if test="employee_work.id != null">
                                #{employee_work.id,jdbcType=BIGINT},
                            </if>
                            <if test="employee_work.name != null">
                                #{employee_work.name,jdbcType=VARCHAR},
                            </if>
                            <if test="employee_work.intotime != null">
                                #{employee_work.intotime,jdbcType=DATE},
                            </if>
                        </trim>;
                    </foreach>
                </if>
            </foreach>
        </if>
    </insert>
</mapper>