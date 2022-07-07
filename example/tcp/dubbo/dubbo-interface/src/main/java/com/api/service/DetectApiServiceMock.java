package com.api.service;

import com.api.dto.common.RequestDTO;
import com.api.dto.common.ResponseDTO;
import com.api.dto.response.FeatureResponse;

public class DetectApiServiceMock implements DetectApiService {
    @Override
    public ResponseDTO<FeatureResponse> detect(RequestDTO<String> request) throws Exception {
        FeatureResponse feature = new FeatureResponse();
        feature.setTop(0D);
        feature.setLeft(0D);
        feature.setLength(0D);
        feature.setWidth(0D);
        ResponseDTO<FeatureResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setCode("200").setBody(feature);
        return responseDTO;
    }
}
