package com.web.controller;

import com.spring.config.SnowFlake;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author : wuheping
 * @ Date   : 2022/3/4
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/snowflake")
@Api(tags = "雪花算法")
@Slf4j
public class SnowFlakeController {

    @Autowired
    private SnowFlake snowFlake;

    @PostMapping("nextId")
    @ApiOperation(value = "雪花算法")
    public ResponseVO nextId() {
        try {
            return ResponseVO.SUCCESS(snowFlake.nextId());
        } catch (Exception e) {
            log.error("nextId 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
