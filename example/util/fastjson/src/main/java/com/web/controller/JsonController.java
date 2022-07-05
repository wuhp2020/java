package com.web.controller;

import com.alibaba.fastjson.JSON;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/com.api/v1/doc")
@Api(tags = "数据转换")
@Slf4j
public class JsonController {

    @ApiOperation(value = "转换")
    @GetMapping("json")
    public ResponseVO json(){
        try {
            JSON.parseObject("{'name': 'wuhp'}", Object.class);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("转换失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
