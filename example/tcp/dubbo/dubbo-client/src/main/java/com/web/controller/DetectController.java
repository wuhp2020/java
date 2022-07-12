package com.web.controller;

import com.api.service.DetectApiService;
import com.api.dto.response.FeatureResponse;
import com.web.service.DetectService;
import com.web.vo.detect.DetectVO;
import com.web.vo.detect.FeatureVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/detect")
@Api(tags = "特征检测")
@Slf4j
public class DetectController {

    @Autowired
    private DetectService detectService;

    @DubboReference(loadbalance = "roundrobin", retries = 2, version = "1.0.0", mock = "true", group = "dubbo-wuhp")
    private DetectApiService detectApiService;

    @ApiOperation(value = "图片base64")
    @PostMapping("base64")
    public FeatureVO detectBase64(@RequestBody DetectVO detectVO) throws Exception {
        FeatureVO featureVO = new FeatureVO();
        FeatureResponse responseDTO = detectApiService.detect(detectVO.getBase64());
        BeanUtils.copyProperties(responseDTO, featureVO);
        return featureVO;
    }


}
