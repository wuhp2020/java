package com.web.controller;

import com.web.service.NodeService;
import com.web.vo.common.ResponseVO;
import com.web.vo.node.NodeQueryVO;
import com.web.vo.node.ConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/node")
@Api(tags = "节点管理")
@Slf4j
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @ApiOperation(value = "添加配置")
    @PostMapping("add")
    public ResponseVO add(@RequestBody ConfigVO configVO) {
        try {
            nodeService.add(configVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("添加节点失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "获取节点")
    @PostMapping("findOne")
    public ResponseVO findOne(@RequestBody NodeQueryVO nodeQueryVO) {
        try {
            List<ServiceInstance> instances = nodeService.findOne(nodeQueryVO);
            return ResponseVO.SUCCESS(instances);
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
