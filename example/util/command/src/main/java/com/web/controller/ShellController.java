package com.web.controller;

import com.web.service.ShellService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shell")
@Api(tags = "shell调用")
@Slf4j
public class ShellController {

    @Autowired
    private ShellService shellService;

    @PostMapping("print")
    @ApiOperation(value = "打印")
    public ResponseVO execPrint() {
        try {
            shellService.execPrint();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:add 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
