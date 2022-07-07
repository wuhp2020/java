package com.web.controller;

import com.web.service.DetectService;
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
@RequestMapping("/api/v1/detect")
@Api(tags = "特征管理")
@Slf4j
public class DetectController {

    @Autowired
    private DetectService detectService;

    @PostMapping("detect")
    @ApiOperation(value = "提取特征")
    public ResponseVO detect(@RequestBody String base64) {
        try {
            return ResponseVO.SUCCESS(detectService.detect(base64));
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
