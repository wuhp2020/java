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
    public OperationLogVO findOne(@PathVariable("id") String id) throws Exception {
        OperationLogVO operationLogVO = operationLogService.findOne(id);
        return operationLogVO;
    }

    @PostMapping("saveLogSetup")
    @ApiOperation(value = "设置日志保存时间")
    public void saveLogSetup(OperationLogSetupVO operationLogSetupVO) throws Exception {
        operationLogSetupVO = operationLogService.saveLogSetup(operationLogSetupVO);
    }

    @GetMapping("findLogSetup")
    @ApiOperation(value = "查询日志保存时间")
    public OperationLogSetupVO findLogSetup() throws Exception {
        OperationLogSetupVO operationLogSetupVO = operationLogService.findLogSetup();
        return operationLogSetupVO;
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询日志")
    public Page<OperationLogVO> findByPage(OperationLogQueryVO operationLogQueryVO) throws Exception {
        Page<OperationLogVO> page = operationLogService.findByPage(operationLogQueryVO);
        return page;
    }

}
