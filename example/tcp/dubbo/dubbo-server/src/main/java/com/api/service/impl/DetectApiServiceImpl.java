package com.api.service.impl;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.api.dto.common.RequestDTO;
import com.api.dto.common.ResponseDTO;
import com.api.service.DetectApiService;
import com.api.dto.response.FeatureResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.util.StringUtils;

@Slf4j
@DubboService(timeout = 10000, loadbalance = "roundrobin", retries = 2, version = "1.0.0", group = "dubbo-wuhp")
public class DetectApiServiceImpl implements DetectApiService {

    @Override
    public ResponseDTO<FeatureResponse> detect(RequestDTO<String> request)  throws Exception {
        log.info("detect ==>> " + JSON.toJSONString(request));
        if (StringUtils.isEmpty(request.getBody())) {
            throw new RpcException("异常啦, 启动降级");
        }
        FeatureResponse feature = new FeatureResponse();
        feature.setTop(0.1D);
        feature.setLeft(1.5D);
        feature.setLength(100.2D);
        feature.setWidth(50.5D);

        ResponseDTO<FeatureResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setCode("200").setBody(feature);
        return responseDTO;
    }
}
