package com.web.controller;

import com.web.service.MagicApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/magicapi")
@Api(tags = "自动生成接口和函数")
@Slf4j
public class MagicApiController {

    @Resource
    private MagicApiService magicApiService;

    @PostMapping("reset")
    @ApiOperation(value = "重置接口和函数")
    public void reset() {
        magicApiService.reset();
    }

}
