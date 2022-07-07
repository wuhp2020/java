package com.web.controller;

import com.web.service.LockService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lock")
@Api(tags = "锁管理")
@Slf4j
public class LockController {

    @Autowired
    private LockService lockService;

    @PostMapping("queue/lock")
    @ApiOperation(value = "队列获取锁")
    public ResponseVO queueLock() {
        try {
            lockService.queueLock();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:lock 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
