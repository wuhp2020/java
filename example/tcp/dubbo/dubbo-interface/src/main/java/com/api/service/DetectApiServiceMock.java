package com.api.service;

import com.api.dto.response.FeatureResponse;

public class DetectApiServiceMock implements DetectApiService {
    @Override
    public FeatureResponse detect(String request) throws Exception {
        FeatureResponse feature = new FeatureResponse();
        feature.setTop(0D);
        feature.setLeft(0D);
        feature.setLength(0D);
        feature.setWidth(0D);
        return feature;
    }
}
