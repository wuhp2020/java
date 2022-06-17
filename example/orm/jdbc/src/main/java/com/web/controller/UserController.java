package com.web.controller;

import com.web.service.UserService;
import com.web.vo.common.ResponseVO;
import com.web.vo.user.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "用户管理")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


}
