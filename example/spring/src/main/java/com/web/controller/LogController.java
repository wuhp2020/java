package com.web.controller;

import com.spring.aspect.Log;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/log")
@Slf4j
@Api(tags = "日志切面")
public class LogController {

    @GetMapping("normalPrint")
    @ApiOperation(value = "正常打印切面日志")
    @Log
    public ResponseVO normalPrint() {
        try {
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:print() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @GetMapping("errorPrint")
    @ApiOperation(value = "错误打印切面日志")
    @Log
    public ResponseVO errorPrint() throws Exception{
        throw new Exception("test抛异常");
    }
}
