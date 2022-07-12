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
    public void lock(@RequestBody LockVO lockVO) throws Exception {
        lockService.lock(lockVO);
    }

    @DeleteMapping("unlock")
    @ApiOperation(value = "释放锁")
    public void unlock(@RequestBody LockVO lockVO) {
        lockService.unlock(lockVO);
    }

}
