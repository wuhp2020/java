package com.web.controller;

import com.api.service.DetectApiService;
import com.web.vo.common.ResponseVO;
import com.web.vo.detect.DetectVO;
import com.web.vo.detect.FeatureVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/com.api/v1/detect")
@Api(tags = "特征检测")
@Slf4j
public class DetectController {

    @GrpcClient("DetectApiService")
    public DetectApiService detectApiService;

    @ApiOperation(value = "图片base64")
    @PostMapping("base64")
    public FeatureVO detectBase64(@RequestBody DetectVO detectVO) {
        FeatureVO featureVO = new FeatureVO();
//            FeatureResponse featureResponse = detectApiService.detect(detectVO.getBase64());
//            BeanUtils.copyProperties(featureResponse, featureVO);
        return featureVO;
    }


}
