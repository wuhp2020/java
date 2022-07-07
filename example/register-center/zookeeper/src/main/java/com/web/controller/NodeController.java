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

    @ApiOperation(value = "添加持久节点")
    @PostMapping("add/persistent")
    public ResponseVO addPersistent(@RequestBody NodeVO nodeVO) {
        try {
            nodeService.addPersistent(nodeVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("添加节点失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "添加持久有序节点")
    @PostMapping("add/persistent/sequential")
    public ResponseVO addPersistentSequential(@RequestBody NodeVO nodeVO) {
        try {
            nodeService.addPersistentSequential(nodeVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("添加节点失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "添加临时节点")
    @PostMapping("add/ephemeral")
    public ResponseVO addEphemeral(@RequestBody NodeVO nodeVO) {
        try {
            nodeService.addEphemeral(nodeVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("添加节点失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "添加临时有序节点")
    @PostMapping("add/ephemeral/sequential")
    public ResponseVO addEphemeralSequential(@RequestBody NodeVO nodeVO) {
        try {
            nodeService.addEphemeralSequential(nodeVO);
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
            return ResponseVO.SUCCESS(nodeService.findOne(nodeQueryVO));
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
