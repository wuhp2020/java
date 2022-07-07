package com.web.controller;

import com.web.service.ProxyService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/proxy")
@Api(tags = "代理")
@Slf4j
public class ProxyController {

    @Autowired
    private ProxyService proxyService;

    @ApiOperation(value = "jdkProxy")
    @GetMapping("jdkProxy")
    public ResponseVO jdkProxy() {
        try {
            StringBuffer sb1 = new StringBuffer();
            StringBuilder sb2 = new StringBuilder();
            sb1.append("a");
            sb2.append("b");
            proxyService.jdkProxy();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:jdkProxy() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "cglib")
    @GetMapping("cglib")
    public ResponseVO cglib() {
        try {
            proxyService.cglib();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:cglib() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
