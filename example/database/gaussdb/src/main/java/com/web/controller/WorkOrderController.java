package com.web.controller;

import com.web.entity.WorkOrderDO;
import com.web.service.WorkOrderService;
import com.web.vo.common.ResponseVO;
import com.web.vo.workorder.WorkOrderBatchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workorder")
@Api(tags = "用户管理")
@Slf4j
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @PostMapping("save")
    @ApiOperation(value = "增加工单")
    public ResponseVO save(WorkOrderBatchVO workOrderBatchVO) {
        try {
            for (int i = workOrderBatchVO.getBegin();i < workOrderBatchVO.getEnd(); i++) {
                WorkOrderDO workOrderDO = new WorkOrderDO();
                workOrderDO.setId(i);
                workOrderDO.setWorkOrderId(UUID.randomUUID().toString());
                workOrderDO.setRelatedSize(5);
                workOrderService.save(workOrderDO);
            }
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @GetMapping("findAll")
    @ApiOperation(value = "所有数据")
    public ResponseVO findAll() {
        try {
            Map<String, Integer> map = workOrderService.findAll();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
