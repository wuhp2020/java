package com.web.controller;

import com.web.service.LockService;
import com.web.vo.common.ResponseVO;
import com.web.vo.lock.LockVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lock")
@Api(tags = "锁管理")
@Slf4j
public class LockController {

    @Autowired
    private LockService lockService;

    @PostMapping("lock")
    @ApiOperation(value = "获取锁")
    public ResponseVO lock(@RequestBody LockVO lockVO) {
        try {
            lockService.lock(lockVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:lock 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @DeleteMapping("unlock")
    @ApiOperation(value = "释放锁")
    public ResponseVO unlock(@RequestBody LockVO lockVO) {
        try {
            lockService.unlock(lockVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:unlock 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
