package com.api.service;

import com.api.dto.response.FeatureResponse;

public interface DetectApiService {

    FeatureResponse detect(String request) throws Exception;
}
