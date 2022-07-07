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
    public ResponseVO exists(@RequestBody String key) {
        try {
            return ResponseVO.SUCCESS(keysService.exists(key));
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("move")
    @ApiOperation(value = "移动")
    public ResponseVO move(@RequestBody String key, @RequestBody Integer index) {
        try {
            return ResponseVO.SUCCESS(keysService.move(key, index));
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("type")
    @ApiOperation(value = "类型")
    public ResponseVO type(@RequestBody String key) {
        try {
            return ResponseVO.SUCCESS(keysService.type(key));
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
