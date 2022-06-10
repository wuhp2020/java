package com.web.controller;

import com.web.service.StoreService;
import com.web.vo.order.PreCreateOrderReqVO;
import com.web.vo.order.PreCreateOrderResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/openele")
@Api(tags = "门店管理")
@Slf4j
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping("chainStoreCreate")
    @ApiOperation(value = "门店创建")
    public PreCreateOrderResVO chainStoreCreate(@RequestBody PreCreateOrderReqVO preCreateOrderReqVO) {
        return null;
    }

    @PostMapping("chainStoreUpdate")
    @ApiOperation(value = "门店更新")
    public PreCreateOrderResVO chainStoreUpdate(@RequestBody PreCreateOrderReqVO preCreateOrderReqVO) {
        return null;
    }

    @PostMapping("chainStoreQuery")
    @ApiOperation(value = "门店查询")
    public PreCreateOrderResVO chainStoreQuery(@RequestBody PreCreateOrderReqVO preCreateOrderReqVO) {
        return null;
    }

    @PostMapping("chainStoreQueryList")
    @ApiOperation(value = "门店查询")
    public PreCreateOrderResVO chainStoreQueryList(@RequestBody PreCreateOrderReqVO preCreateOrderReqVO) {
        return null;
    }

    @PostMapping("chainStoreRange")
    @ApiOperation(value = "查询配送范围")
    public PreCreateOrderResVO chainStoreRange(@RequestBody PreCreateOrderReqVO preCreateOrderReqVO) {
        return null;
    }

    @PostMapping("newUploadFile")
    @ApiOperation(value = "图片上传")
    public PreCreateOrderResVO newUploadFile(@RequestBody PreCreateOrderReqVO preCreateOrderReqVO) {
        return null;
    }
}
