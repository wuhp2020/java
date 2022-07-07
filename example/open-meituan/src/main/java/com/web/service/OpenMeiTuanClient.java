package com.web.service;

import com.google.gson.Gson;
import com.web.util.HttpClientUtil;
import com.web.vo.common.OpenMeiTuanReqVO;
import com.web.vo.common.OpenMeiTuanResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ Author : wuheping
 * @ Date   : 2022/3/10
 * @ Desc   : 描述
 */
@Service
@Slf4j
public class OpenMeiTuanClient {

    private Gson gson = new Gson();

    public <T> T httpPost(String url, OpenMeiTuanReqVO param, Class<T> cls) {
        param.setTimestamp(System.currentTimeMillis());
        String json = HttpClientUtil.httpPostUrlEncoded(url, param);
        OpenMeiTuanResVO resVO = gson.fromJson(json, OpenMeiTuanResVO.class);
        if (!"0".equals(resVO.getCode())) {
            throw new RuntimeException(resVO.getMessage());
        }
        return gson.fromJson(resVO.getData(), cls);
    }
}
