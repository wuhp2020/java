package com.web.controller;

import com.web.service.TaskService;
import com.web.vo.common.ResponseVO;
import com.web.vo.task.StartTaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task")
@Api(tags = "账户服务")
@Slf4j
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "start")
    @ApiOperation(value = "开启任务")
    public ResponseVO start(@RequestBody StartTaskVO startTaskVO) {
        try {
            taskService.start(startTaskVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:start 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
