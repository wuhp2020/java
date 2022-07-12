package com.web.controller;

import com.web.service.OrderService;
import com.web.vo.common.ResponseVO;
import com.web.vo.order.OrderRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@Api(tags = "订单服务")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("create")
    @ApiOperation(value = "生成订单")
    public void create(@RequestBody OrderRequestVO orderRequestVO) {
        orderService.create(orderRequestVO);
    }

}
