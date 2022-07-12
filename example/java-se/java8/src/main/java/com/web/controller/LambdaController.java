package com.web.controller;

import com.web.service.LambdaService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lambda")
@Api(tags = "表达式")
@Slf4j
public class LambdaController {

    @Autowired
    private LambdaService lambdaService;

    @ApiOperation(value = "判断是否是有效的lambda")
    @PostMapping("isLambda")
    public void isLambda() {
        lambdaService.isLambda();
    }

    @ApiOperation(value = "lambda案例")
    @PostMapping("lambdaExample")
    public void lambdaExample() {
        lambdaService.lambdaExample();
    }

}
