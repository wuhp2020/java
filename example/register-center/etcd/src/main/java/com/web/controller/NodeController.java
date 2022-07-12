package com.web.controller;

import com.web.service.NodeService;
import com.web.vo.common.ResponseVO;
import com.web.vo.node.NodeQueryVO;
import com.web.vo.node.NodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/node")
@Api(tags = "节点管理")
@Slf4j
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @ApiOperation(value = "添加节点")
    @PostMapping("add")
    public void add(@RequestBody NodeVO nodeVO) throws Exception {
        nodeService.add(nodeVO);
    }

    @ApiOperation(value = "获取节点")
    @PostMapping("findOne")
    public Object findOne(@RequestBody NodeQueryVO nodeQueryVO) throws Exception {
        return nodeService.findOne(nodeQueryVO);
    }

    @ApiOperation(value = "获取所有节点")
    @PostMapping("findAll")
    public Object findAll() throws Exception {
        return nodeService.findAll();
    }
}
