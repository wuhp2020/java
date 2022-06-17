package com.web.waybill.controller;

import com.web.common.vo.ResponseVO;
import com.web.waybill.service.WaybillService;
import com.web.waybill.vo.waybill.WaybillBatchVO;
import com.web.waybill.vo.waybill.WaybillUpdateStatusVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/waybill")
@Api(tags = "运单管理")
@Slf4j
public class WaybillController {

    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(4, 4,
            10, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    @Autowired
    private WaybillService waybillService;

    @PostMapping("save")
    @ApiOperation(value = "批量增加订单")
    public ResponseVO save(WaybillBatchVO waybillBatchVO) {
        try {

            for (int i = waybillBatchVO.getBegin();i < waybillBatchVO.getEnd(); i++) {
                final int j = i;
                log.info("提交任务: " + j);
                poolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            waybillService.save(j);
                        } catch (Exception e) {
                            log.error("错误: ", e);
                        }
                    }
                });
            }
            return ResponseVO.SUCCESS("ok");
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PutMapping("updateStatus")
    @ApiOperation(value = "更新状态")
    public ResponseVO updateStatus(@RequestBody WaybillUpdateStatusVO statusVO) {
        try {
            waybillService.updateStatus(statusVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
