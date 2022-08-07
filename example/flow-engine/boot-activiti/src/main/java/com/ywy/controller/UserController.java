package com.ywy.controller;

import com.ywy.dao.UserDao;
import com.ywy.entity.UserInfoEntity;
import com.ywy.rest.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户Controller
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-08 16:59
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserDao userDao;

    /**
     * 获取用户列表
     * @return
     */
    @GetMapping("list")
    public Result list() {
        try {
            List<UserInfoEntity> list = userDao.selectUsers();
            return Result.success("获取用户列表成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("获取用户列表失败");
        }
    }
}
