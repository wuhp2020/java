package com.web.controller;

import com.web.service.AlertService;
import com.web.vo.alert.AlertVO;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alert")
@Api(tags = "告警管理")
@Slf4j
public class AlertController {

    @Autowired
    private AlertService alertService;

    @ApiOperation(value = "发送消息")
    @PostMapping("send")
    public void send(@RequestBody AlertVO alertVO) {

    }
}
