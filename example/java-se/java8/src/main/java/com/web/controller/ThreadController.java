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
    public void printABC() throws Exception {
        threadService.printABC();
    }

    @ApiOperation(value = "创建线程池")
    @GetMapping("createThreadPool")
    public void createThreadPool() throws Exception {
        threadService.createThreadPool();
    }

    @ApiOperation(value = "线程封闭工具")
    @GetMapping("threadLocal")
    public void threadLocal() throws Exception {
        threadService.threadLocal();
    }

    @ApiOperation(value = "线程ID")
    @GetMapping("threadID")
    public void threadID() {
        log.info("=====>>>>> " + Thread.currentThread().getId());
    }

    @ApiOperation(value = "线程合并")
    @GetMapping("join")
    public void join() throws Exception {
        threadService.join();
        log.info("=====>>>>> " + Thread.currentThread().getId());
    }
}
