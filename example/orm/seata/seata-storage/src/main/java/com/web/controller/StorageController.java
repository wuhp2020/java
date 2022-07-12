package com.web.controller;

import com.web.service.StorageService;
import com.web.vo.common.ResponseVO;
import com.web.vo.storage.StorageRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/storage")
@Api(tags = "仓储服务")
@Slf4j
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("deduct")
    @ApiOperation(value = "扣除存储数量")
    public void deduct(@RequestBody StorageRequestVO storageRequestVO) {
        storageService.deduct(storageRequestVO);
    }
}
