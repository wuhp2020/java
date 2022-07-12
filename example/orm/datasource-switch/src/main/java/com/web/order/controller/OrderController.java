package com.web.order.controller;

import com.web.order.entity.OrderDO;
import com.web.order.service.OrderService;
import com.web.vo.order.OrderBatchVO;
import com.web.vo.order.OrderUpdateStatusVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/order")
@Api(tags = "订单管理")
@Slf4j
public class OrderController {

    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(4, 4,
            10, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    @Autowired
    private OrderService orderService;

    @PostMapping("save")
    @ApiOperation(value = "批量增加订单")
    public void save(OrderBatchVO orderBatchVO) {
        for (int i = orderBatchVO.getBegin();i < orderBatchVO.getEnd(); i++) {
            final int j = i;
            log.info("提交任务: " + j);
            poolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        orderService.save(j);
                    } catch (Exception e) {
                        log.error("错误: ", e);
                    }
                }
            });
        }
    }

    @PutMapping("updateStatus")
    @ApiOperation(value = "更新状态")
    public void updateStatus(@RequestBody OrderUpdateStatusVO statusVO) {
        orderService.updateStatus(statusVO);
    }

    @GetMapping("findByOrderNo/{orderNo}")
    @ApiOperation(value = "根据订单号查询")
    public OrderDO findByOrderNo(@PathVariable("orderNo") String orderNo) {
        return orderService.findByOrderNo(orderNo);
    }

    @GetMapping("findByTakeGoodsCode/{takeGoodsCode}")
    @ApiOperation(value = "根据取件码查询")
    public OrderDO findByTakeGoodsCode(@PathVariable("takeGoodsCode") String takeGoodsCode) {
        return orderService.findByTakeGoodsCode(takeGoodsCode);
    }

}
