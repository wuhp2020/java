package com.web.controller;

import com.web.service.InterfaceService;
import com.web.vo.InterfaceReqVO;
import com.web.vo.InterfaceResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @ApiOperation(value = "创建接口")
    @PostMapping(value = "/save")
    public void saveInterface(@RequestBody InterfaceReqVO reqVO) {
        interfaceService.saveInterface(reqVO);
    }

    @ApiOperation(value = "删除接口")
    @DeleteMapping(value = "/delete/{id}")
    public void deleteInterface(@PathVariable String id) {
        interfaceService.deleteInterface(id);
    }

    @ApiOperation(value = "查看单个接口")
    @GetMapping(value = "/getById/{id}")
    public InterfaceResVO findInterfaceById(@PathVariable String id) {
        return interfaceService.findInterfaceById(id);
    }
}
