package com.web.controller;

import com.web.service.StoreService;
import com.web.vo.PageResVO;
import com.web.vo.store.ChainStoreCreateReqVO;
import com.web.vo.store.ChainStoreCreateResVO;
import com.web.vo.store.ChainStoreQueryListReqVO;
import com.web.vo.store.ChainStoreQueryListResVO;
import com.web.vo.store.ChainStoreQueryReqVO;
import com.web.vo.store.ChainStoreQueryResVO;
import com.web.vo.store.ChainStoreRangeReqVO;
import com.web.vo.store.ChainStoreRangeResVO;
import com.web.vo.store.ChainStoreUpdateReqVO;
import com.web.vo.store.ChainStoreUpdateResVO;
import com.web.vo.store.NewUploadFileReqVO;
import com.web.vo.store.NewUploadFileResVO;
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
    public ChainStoreCreateResVO chainStoreCreate(@RequestBody ChainStoreCreateReqVO chainStoreCreateReqVO) {
        return storeService.chainStoreCreate(chainStoreCreateReqVO);
    }

    @PostMapping("chainStoreUpdate")
    @ApiOperation(value = "门店更新")
    public ChainStoreUpdateResVO chainStoreUpdate(@RequestBody ChainStoreUpdateReqVO chainStoreUpdateReqVO) {
        return null;
    }

    @PostMapping("chainStoreQuery")
    @ApiOperation(value = "门店查询")
    public ChainStoreQueryResVO chainStoreQuery(@RequestBody ChainStoreQueryReqVO chainStoreQueryReqVO) {
        return storeService.chainStoreQuery(chainStoreQueryReqVO);
    }

    @PostMapping("chainStoreQueryList")
    @ApiOperation(value = "门店查询")
    public PageResVO<ChainStoreQueryListResVO> chainStoreQueryList(@RequestBody ChainStoreQueryListReqVO chainStoreQueryListReqVO) {
        return storeService.chainStoreQueryList(chainStoreQueryListReqVO);
    }

    @PostMapping("chainStoreRange")
    @ApiOperation(value = "查询配送范围")
    public ChainStoreRangeResVO chainStoreRange(@RequestBody ChainStoreRangeReqVO chainStoreRangeReqVO) {
        return null;
    }

    @PostMapping("newUploadFile")
    @ApiOperation(value = "图片上传")
    public NewUploadFileResVO newUploadFile(@RequestBody NewUploadFileReqVO uploadFileReqVO) {
        return null;
    }
}
