package com.web.controller;

import com.web.service.InterfaceService;
import com.web.vo.InterfaceVO;
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
@RequestMapping("/api/v1/interface")
@Api(tags = "接口管理")
@Slf4j
public class InterfaceController {

    @Autowired
    private InterfaceService interfaceService;

    @PostMapping("create")
    @ApiOperation(value = "创建接口")
    public ResponseVO create(@RequestBody InterfaceVO createInterfaceVO) {
        try {
            interfaceService.create(createInterfaceVO);
            return ResponseVO.SUCCESS("ok");
        } catch (Exception e) {
            log.error("异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
