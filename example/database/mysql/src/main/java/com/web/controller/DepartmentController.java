package com.web.controller;

import com.web.entity.DepartmentDO;
import com.web.service.DepartmentService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
@Api(tags = "部门管理")
@Slf4j
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("selectAll")
    @ApiOperation(value = "查询部门")
    public List<DepartmentDO> selectAll() {
        List<DepartmentDO> data = departmentService.selectAll();
        return data;
    }
}
