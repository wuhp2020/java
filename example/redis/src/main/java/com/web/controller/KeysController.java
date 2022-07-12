package com.web.controller;

import com.web.service.KeysService;
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
@RequestMapping("/api/v1/keys")
@Api(tags = "keys命令")
@Slf4j
public class KeysController {

    @Autowired
    private KeysService keysService;

    @PostMapping("exists")
    @ApiOperation(value = "是否存在")
    public void exists(@RequestBody String key) {
        keysService.exists(key);
    }

    @PostMapping("move")
    @ApiOperation(value = "移动")
    public void move(@RequestBody String key, @RequestBody Integer index) {
        keysService.move(key, index);
    }

    @PostMapping("type")
    @ApiOperation(value = "类型")
    public void type(@RequestBody String key) {
        keysService.type(key);
    }
}
