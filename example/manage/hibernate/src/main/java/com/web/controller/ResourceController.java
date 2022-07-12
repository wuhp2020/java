package com.web.controller;

import com.web.service.ResourceService;
import com.web.vo.common.ResponseVO;
import com.web.vo.resource.ResourceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resource")
@Slf4j
@Api(tags = "资源管理")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("findResource")
    @ApiOperation(value = "查询资源")
    public List<ResourceVO> findResource() throws Exception {
        List<ResourceVO> resourceVOs = resourceService.findResource();
        return resourceVOs;
    }
}
