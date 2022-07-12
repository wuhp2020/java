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
    public void create(TaskVO taskVO) throws Exception {
        taskService.create(taskVO);
    }

    @PostMapping("start")
    @ApiOperation(value = "启动任务")
    public void start(String id) throws Exception {
        taskService.start(id);
    }

    @PostMapping("stop")
    @ApiOperation(value = "停止任务")
    public void stop(String id) throws Exception {
        taskService.stop(id);
    }
}
