package com.api.service;

import com.api.dto.common.RequestDTO;
import com.api.dto.common.ResponseDTO;
import com.api.dto.response.FeatureResponse;

public interface DetectApiService {

    ResponseDTO<FeatureResponse> detect(RequestDTO<String> request) throws Exception;
}
