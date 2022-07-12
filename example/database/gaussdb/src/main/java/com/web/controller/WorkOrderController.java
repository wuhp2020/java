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
    public void save(WorkOrderBatchVO workOrderBatchVO) {
        for (int i = workOrderBatchVO.getBegin();i < workOrderBatchVO.getEnd(); i++) {
            WorkOrderDO workOrderDO = new WorkOrderDO();
            workOrderDO.setId(i);
            workOrderDO.setWorkOrderId(UUID.randomUUID().toString());
            workOrderDO.setRelatedSize(5);
            workOrderService.save(workOrderDO);
        }
    }

    @GetMapping("findAll")
    @ApiOperation(value = "所有数据")
    public Map<String, Integer> findAll() {
        Map<String, Integer> map = workOrderService.findAll();
        return map;
    }

}
