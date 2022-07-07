package com.web.controller;

import com.web.service.ThreadService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/thread")
@Api(tags = "线程")
@Slf4j
public class ThreadController {

    @Autowired
    private ThreadService threadService;

    @ApiOperation(value = "ABC循环打印")
    @PostMapping("abc/print")
    public ResponseVO printABC() {
        try {
            threadService.printABC();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:printABC() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "创建线程池")
    @GetMapping("createThreadPool")
    public ResponseVO createThreadPool() {
        try {
            threadService.createThreadPool();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:createThreadPool() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "线程封闭工具")
    @GetMapping("threadLocal")
    public ResponseVO threadLocal() {
        try {
            threadService.threadLocal();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:threadLocal() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "线程ID")
    @GetMapping("threadID")
    public ResponseVO threadID() {
        try {
            return ResponseVO.SUCCESS(Thread.currentThread().getId());
        } catch (Exception e) {
            log.error("method:threadID() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "线程合并")
    @GetMapping("join")
    public ResponseVO join() {
        try {
            threadService.join();
            return ResponseVO.SUCCESS(Thread.currentThread().getId());
        } catch (Exception e) {
            log.error("method:threadID() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
