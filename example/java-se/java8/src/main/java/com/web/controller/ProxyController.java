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
    public void jdkProxy() {
        StringBuffer sb1 = new StringBuffer();
        StringBuilder sb2 = new StringBuilder();
        sb1.append("a");
        sb2.append("b");
        proxyService.jdkProxy();
    }

    @ApiOperation(value = "cglib")
    @GetMapping("cglib")
    public void cglib() {
        proxyService.cglib();
    }
}
