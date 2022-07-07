package com.web.service;

import com.google.gson.Gson;
import com.spring.config.OpenEleConfig;
import com.web.vo.common.OpenEleReqVO;
import com.web.vo.common.OpenEleResVO;
import com.web.vo.token.OpenEleTokenReqVO;
import com.web.util.HttpClientUtil;
import com.web.util.SignatureUtil;
import com.web.vo.token.OpenEleTokenResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/28
 * @ Desc   : 描述
 */
@Slf4j
@Service
public class OpenEleClient {
    @Autowired
    private OpenEleConfig openEleConfig;

    private Gson gson = new Gson();

    private OpenEleTokenResVO getToken(String timestamp) {
        String signature = SignatureUtil.genToken(openEleConfig.getGrantType(), openEleConfig.getCode(),
                openEleConfig.getAppId(), openEleConfig.getMerchantId(), timestamp, openEleConfig.getAppSecret());
        OpenEleTokenReqVO tokenReqDTO = new OpenEleTokenReqVO();
        tokenReqDTO.setGrant_type(openEleConfig.getGrantType()).setCode(openEleConfig.getCode())
                .setApp_id(openEleConfig.getAppId()).setSignature(signature)
                .setMerchant_id(openEleConfig.getMerchantId()).setTimestamp(timestamp);
        String json = HttpClientUtil.httpPost(openEleConfig.getTokenUrl(), gson.toJson(tokenReqDTO));
        OpenEleResVO resDTO = gson.fromJson(json, OpenEleResVO.class);
        String bd = resDTO.getBusiness_data().substring(0, resDTO.getBusiness_data().length());
        OpenEleTokenResVO tokenResDTO = gson.fromJson(bd, OpenEleTokenResVO.class);
        return tokenResDTO;
    }

    public <T> T httpPost(String url, Object param, Class<T> cls) {
        String timestamp = System.currentTimeMillis() + "";
        OpenEleTokenResVO tokenResDTO = this.getToken(timestamp);
        OpenEleReqVO openEleReqDTO = new OpenEleReqVO();
        openEleReqDTO.setApp_id(openEleConfig.getAppId());
        openEleReqDTO.setMerchant_id(openEleConfig.getMerchantId());
        String businessData = gson.toJson(param);
        String signature = SignatureUtil.genSignature(openEleConfig.getAppId(), openEleConfig.getMerchantId(),
                tokenResDTO.getAccess_token(), timestamp, businessData, openEleConfig.getAppSecret());
        openEleReqDTO.setSignature(signature).setTimestamp(timestamp)
                .setAccess_token(tokenResDTO.getAccess_token()).setBusiness_data(businessData);
        String json = HttpClientUtil.httpPost(url, gson.toJson(openEleReqDTO));
        OpenEleResVO resDTO = gson.fromJson(json, OpenEleResVO.class);
        if (!"200".equals(resDTO.getCode())) {
            throw new RuntimeException(resDTO.getMsg());
        }
        String bd = resDTO.getBusiness_data().substring(0, resDTO.getBusiness_data().length());
        return gson.fromJson(bd, cls);
    }
}
