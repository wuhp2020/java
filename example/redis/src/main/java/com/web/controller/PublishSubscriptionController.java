package com.web.controller;

import com.web.service.PublishSubscriptionService;
import com.web.vo.common.ResponseVO;
import com.web.vo.lock.LockVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/publish/subscription")
@Api(tags = "发布订阅")
@Slf4j
public class PublishSubscriptionController {

    @Autowired
    private PublishSubscriptionService publishSubscriptionService;

    @PostMapping("alert")
    @ApiOperation(value = "告警")
    public ResponseVO alert(@RequestBody String message) {
        try {
            publishSubscriptionService.alert(message);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:alert 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
