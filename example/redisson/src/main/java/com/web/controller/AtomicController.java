package com.web.controller;

import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/21
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/atomic")
@Api(tags = "原子")
@Slf4j
public class AtomicController {

    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("atomicLong")
    @ApiOperation(value = "分布式AtomicLong")
    public void atomicLong() throws Exception {
        RAtomicLong atomicLong = redissonClient.getAtomicLong("myAtomicLong");
        atomicLong.set(3);
        atomicLong.incrementAndGet();
        atomicLong.get();
    }

    @PostMapping("atomicDouble")
    @ApiOperation(value = "分布式AtomicDouble")
    public void atomicDouble() throws Exception {
        RAtomicDouble atomicDouble = redissonClient.getAtomicDouble("myAtomicDouble");
        atomicDouble.set(2.81);
        atomicDouble.addAndGet(4.11);
    }
}
