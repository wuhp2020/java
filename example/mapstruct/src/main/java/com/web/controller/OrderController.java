package com.web.controller;

import com.web.convert.OrderDataMapper;
import com.web.entity.OrderDO;
import com.web.vo.common.ResponseVO;
import com.web.vo.order.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@Api(tags = "订单管理")
@Slf4j
public class OrderController {

    @GetMapping("convert")
    @ApiOperation(value = "转换")
    public ResponseVO convert() {
        try {
            OrderVO orderVO = OrderDataMapper.INSTANCES.orderDO2OrderVO(new OrderDO());
            return ResponseVO.SUCCESS(orderVO);
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
