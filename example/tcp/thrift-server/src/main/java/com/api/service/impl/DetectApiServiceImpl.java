package com.api.service.impl;

import com.api.vo.response.FeatureResponse;

public class DetectApiServiceImpl {

    public FeatureResponse detect(String base64)  throws Exception {
        FeatureResponse feature = new FeatureResponse();
        feature.setTop(0.1D);
        feature.setLeft(1.5D);
        feature.setLength(100.2D);
        feature.setWidth(50.5D);
        return feature;
    }
}
