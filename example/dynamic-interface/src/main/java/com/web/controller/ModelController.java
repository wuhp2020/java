package com.web.controller;

import com.web.vo.ModelVO;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/model")
@Api(tags = "模型管理")
@Slf4j
public class ModelController {


    @PostMapping("create")
    @ApiOperation(value = "创建模型")
    public ResponseVO create(@RequestBody ModelVO createModelVO) {
        try {
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
