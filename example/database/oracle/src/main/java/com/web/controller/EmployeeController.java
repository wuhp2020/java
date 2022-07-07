package com.web.controller;

import com.web.service.EmployeeService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@Api(tags = "员工管理")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("batch/save")
    @ApiOperation(value = "批量增加10w员工")
    public ResponseVO batchSave() {
        try {

            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:batchSave 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("index/over")
    @ApiOperation(value = "覆盖索引")
    public ResponseVO overIndex() {
        try {

            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:overIndex 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("index/oneColumn")
    @ApiOperation(value = "单列索引")
    public ResponseVO oneColumnIndex() {
        try {

            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:oneColumnIndex 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("index/manyColumn")
    @ApiOperation(value = "组合索引")
    public ResponseVO manyColumnIndex() {
        try {

            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:manyColumnIndex 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
