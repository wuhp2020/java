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

import java.util.List;

@RestController
@RequestMapping("/api/v1/node")
@Api(tags = "节点管理")
@Slf4j
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @ApiOperation(value = "添加持久节点")
    @PostMapping("add/persistent")
    public void addPersistent(@RequestBody NodeVO nodeVO) throws Exception {
        nodeService.addPersistent(nodeVO);
    }

    @ApiOperation(value = "添加持久有序节点")
    @PostMapping("add/persistent/sequential")
    public void addPersistentSequential(@RequestBody NodeVO nodeVO) throws Exception {
        nodeService.addPersistentSequential(nodeVO);
    }

    @ApiOperation(value = "添加临时节点")
    @PostMapping("add/ephemeral")
    public void addEphemeral(@RequestBody NodeVO nodeVO) throws Exception {
        nodeService.addEphemeral(nodeVO);
    }

    @ApiOperation(value = "添加临时有序节点")
    @PostMapping("add/ephemeral/sequential")
    public void addEphemeralSequential(@RequestBody NodeVO nodeVO) throws Exception {
        nodeService.addEphemeralSequential(nodeVO);
    }

    @ApiOperation(value = "获取节点")
    @PostMapping("findOne")
    public List<String> findOne(@RequestBody NodeQueryVO nodeQueryVO) throws Exception {
        return nodeService.findOne(nodeQueryVO);
    }
}
