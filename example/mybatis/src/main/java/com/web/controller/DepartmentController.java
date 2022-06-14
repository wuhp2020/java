package com.web.controller;

import com.web.service.DepartmentService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/department")
@Api(tags = "部门管理")
@Slf4j
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("select")
    @ApiOperation(value = "查询部门关联员工")
    public ResponseVO select() {
        List<Map<String, Object>> data = departmentService.select();
        return ResponseVO.SUCCESS(data);
    }

    @GetMapping("selectObject")
    @ApiOperation(value = "查询部门和员工")
    public ResponseVO selectObject() {
        Map<String, Object> data = departmentService.selectObject();
        return ResponseVO.SUCCESS(data);
    }

    @PostMapping("insert")
    @ApiOperation(value = "添加部门、员工和工时")
    public ResponseVO insert(@RequestBody Map<String, Object> paramMap) {
        departmentService.insert(paramMap);
        return ResponseVO.SUCCESS("ok");
    }
}
