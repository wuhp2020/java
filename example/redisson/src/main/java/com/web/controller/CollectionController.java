package com.web.controller;

import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RFuture;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/21
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/collection")
@Api(tags = "集合")
@Slf4j
public class CollectionController {
    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("map")
    @ApiOperation(value = "分布式Map")
    public ResponseVO map() throws Exception {
        RMap<String, Object> map = redissonClient.getMap("anyMap");
        map.put("123", new Object());
        map.putIfAbsent("323", new Object());
        Object obj = map.remove("123");
        map.fastPut("321", new Object());
        map.fastRemove("321");
        RFuture<Object> putAsyncFuture = map.putAsync("321", new Object());
        RFuture<Boolean> fastPutAsyncFuture = map.fastPutAsync("321", new Object());
        map.fastPutAsync("321", new Object());
        map.fastRemoveAsync("321");
        return ResponseVO.SUCCESS("ok");
    }

    @PostMapping("set")
    @ApiOperation(value = "分布式Set")
    public ResponseVO set() throws Exception {
        RSet<Object> set = redissonClient.getSet("anySet");
        set.add(new Object());
        set.remove(new Object());
        return ResponseVO.SUCCESS("ok");
    }

    @PostMapping("list")
    @ApiOperation(value = "分布式List")
    public ResponseVO list() throws Exception {
        RList<Object> list = redissonClient.getList("anyList");
        list.add(new Object());
        list.get(0);
        list.remove(new Object());
        return ResponseVO.SUCCESS("ok");
    }

    @PostMapping("blockingQueue")
    @ApiOperation(value = "分布式Blocking Queue")
    public ResponseVO blockingQueue() throws Exception {
        RBlockingQueue<Object> queue = redissonClient.getBlockingQueue("anyQueue");
        queue.offer(new Object());
        Object obj = queue.peek();
        Object someObj = queue.poll();
        Object ob = queue.poll(10, TimeUnit.MINUTES);
        return ResponseVO.SUCCESS("ok");
    }
}
