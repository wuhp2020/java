package com.web.controller;

import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/lock")
@Api(tags = "锁")
@Slf4j
public class LockController {

    @Autowired
    private CuratorFramework client;

    @PostMapping("reentrantLock")
    @ApiOperation(value = "可重入锁")
    public void reentrantLock() throws Exception {
        InterProcessMutex lock = new InterProcessMutex(client, "/lock/1");
        try {
            if (lock.acquire(10, TimeUnit.SECONDS)) {
                Thread.sleep(5000);
                log.info("=========== 获取锁成功");
                if (client.checkExists().forPath("/lock/1") != null) {
                    client.delete().deletingChildrenIfNeeded().forPath("/lock/1");
                }
            }
        } catch (Exception e) {
            log.error("异常", e);
        } finally {
            if (lock.isAcquiredInThisProcess()) {
                lock.release();
            }
        }
    }
}
