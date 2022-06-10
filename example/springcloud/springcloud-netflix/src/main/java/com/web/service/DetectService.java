package com.web.service;

import com.web.vo.detect.FeatureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DetectService {

    public FeatureResponse detect(String base64) {
        FeatureResponse feature = new FeatureResponse();
        feature.setTop(0.1D);
        feature.setLeft(1.5D);
        feature.setLength(100.2D);
        feature.setWidth(50.5D);
        return feature;
    }

}
