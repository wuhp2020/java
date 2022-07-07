package com.web.service;

import com.spring.config.OpenEleConfig;
import com.web.vo.common.PageResVO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@Service
@Slf4j
public class StoreService {

    @Autowired
    private OpenEleConfig openEleUrlConfig;

    @Autowired
    private OpenEleClient openEleClient;

    public ChainStoreCreateResVO chainStoreCreate(ChainStoreCreateReqVO chainStoreCreateReqVO) {
        ChainStoreCreateResVO storeCreateResVO = openEleClient.httpPost(
                openEleUrlConfig.getStoreCreateUrl(), chainStoreCreateReqVO, ChainStoreCreateResVO.class);
        return storeCreateResVO;
    }

    public ChainStoreUpdateResVO chainStoreUpdate(ChainStoreUpdateReqVO chainStoreUpdateReqVO) {
        return null;
    }

    public ChainStoreQueryResVO chainStoreQuery(ChainStoreQueryReqVO chainStoreQueryReqVO) {
        ChainStoreQueryResVO storeQueryResVO = openEleClient.httpPost(
                openEleUrlConfig.getStoreQueryUrl(), chainStoreQueryReqVO, ChainStoreQueryResVO.class);
        return storeQueryResVO;
    }

    public PageResVO<ChainStoreQueryListResVO> chainStoreQueryList(ChainStoreQueryListReqVO chainStoreQueryListReqVO) {
        PageResVO<ChainStoreQueryListResVO> storeQueryResVO = openEleClient.httpPost(
                openEleUrlConfig.getStoreQueryListUrl(), chainStoreQueryListReqVO, PageResVO.class);
        return storeQueryResVO;
    }

    public ChainStoreRangeResVO chainStoreRange(ChainStoreRangeReqVO chainStoreRangeReqVO) {
        return null;
    }

    public NewUploadFileResVO newUploadFile(NewUploadFileReqVO uploadFileReqVO) {
        return null;
    }
}
