package com.api.service;

import com.api.vo.response.FeatureResponse;

public interface DetectApiService {

    FeatureResponse detect(String base64) throws Exception;
}
