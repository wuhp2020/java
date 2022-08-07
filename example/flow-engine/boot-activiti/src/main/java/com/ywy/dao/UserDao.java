package com.ywy.dao;

import com.ywy.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-02 17:04
 */
@Mapper
public interface UserDao {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from user where username = #{username}")
    UserInfoEntity selectByUsername(String username);

    /**
     * 查询用户列表
     * @return
     */
    @Select("select name, username from user")
    List<UserInfoEntity> selectUsers();
}
