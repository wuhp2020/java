package com.web.controller;

import com.web.service.DataService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/data")
@Api(tags = "数据管理")
@Slf4j
public class DataController {

    @Autowired
    private DataService dataService;

    @PostMapping("search")
    @ApiOperation(value = "获取系统类型")
    public ResponseVO search() {
        try {
            String result = dataService.search();
            return ResponseVO.SUCCESS(result);
        } catch (Exception e) {
            log.error("method:add 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
