package com.web.controller;

import com.web.service.PythonService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/python")
@Api(tags = "python调用")
@Slf4j
public class PythonController {

    @Autowired
    private PythonService pythonService;

    @PostMapping("print")
    @ApiOperation(value = "打印")
    public void execPrint() throws Exception {
        pythonService.execPrint();
    }
}
