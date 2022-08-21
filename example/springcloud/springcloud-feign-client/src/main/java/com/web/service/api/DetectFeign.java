package com.web.service.api;

import com.web.vo.common.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "service1")
public interface DetectFeign {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/detect/detect")
    ResponseVO detect(String base64);
}
