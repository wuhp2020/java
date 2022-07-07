package com.web.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/21
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/object")
@Api(tags = "object")
@Slf4j
public class ObjectController {

    @Autowired
    private RedissonClient redissonClient;
}
