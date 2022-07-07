package com.web.controller;

import com.web.service.TaskService;
import com.web.vo.common.ResponseVO;
import com.web.vo.task.TaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task")
@Slf4j
@Api(tags = "任务管理")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("create")
    @ApiOperation(value = "创建任务")
    public ResponseVO create(TaskVO taskVO) {
        try {
            taskService.create(taskVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:create() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("start")
    @ApiOperation(value = "启动任务")
    public ResponseVO start(String id) {
        try {
            taskService.start(id);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:start() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("stop")
    @ApiOperation(value = "停止任务")
    public ResponseVO stop(String id) {
        try {
            taskService.stop(id);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:stop() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
