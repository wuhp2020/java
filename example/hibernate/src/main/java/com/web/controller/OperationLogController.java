package com.web.controller;

import com.web.service.OperationLogService;
import com.web.vo.common.ResponseVO;
import com.web.vo.operationlog.OperationLogQueryVO;
import com.web.vo.operationlog.OperationLogSetupVO;
import com.web.vo.operationlog.OperationLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/operationlog")
@Slf4j
@Api(tags = "日志管理")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping("findOne/{id}")
    @ApiOperation(value = "查询单个日志")
    public ResponseVO findOne(@PathVariable("id") String id) {
        try {
            OperationLogVO operationLogVO = operationLogService.findOne(id);
            return ResponseVO.SUCCESS(operationLogVO);
        } catch (Exception e) {
            log.error("class:OperationLogController, method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("saveLogSetup")
    @ApiOperation(value = "设置日志保存时间")
    public ResponseVO saveLogSetup(OperationLogSetupVO operationLogSetupVO) {
        try {
            operationLogSetupVO = operationLogService.saveLogSetup(operationLogSetupVO);
            return ResponseVO.SUCCESS(operationLogSetupVO);
        } catch (Exception e) {
            log.error("class:OperationLogController, method:saveLogSetup 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @GetMapping("findLogSetup")
    @ApiOperation(value = "查询日志保存时间")
    public ResponseVO findLogSetup() {
        try {
            OperationLogSetupVO operationLogSetupVO = operationLogService.findLogSetup();
            return ResponseVO.SUCCESS(operationLogSetupVO);
        } catch (Exception e) {
            log.error("class:OperationLogController, method:findLogSetup 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询日志")
    public ResponseVO findByPage(OperationLogQueryVO operationLogQueryVO) {
        try {
            Page<OperationLogVO> page = operationLogService.findByPage(operationLogQueryVO);
            return ResponseVO.SUCCESS(page);
        } catch (Exception e) {
            log.error("class:OperationLogController, method:findByPage 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
