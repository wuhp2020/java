package com.web.controller;

import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/detect")
@Api(tags = "特征管理")
@Slf4j
public class DetectController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String REST_URL_PREFIX = "http://SERVICE1";

    @PostMapping("detect")
    @ApiOperation(value = "提取特征")
    public Object detect(@RequestBody String base64) {
        return restTemplate.postForObject(REST_URL_PREFIX + "/api/v1/detect/detect", base64, ResponseVO.class);
    }

}
