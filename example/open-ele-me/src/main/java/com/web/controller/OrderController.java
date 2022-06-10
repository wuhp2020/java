package com.web.controller;

import com.web.service.OrderService;
import com.web.vo.order.AddTipReqVO;
import com.web.vo.order.AddTipResVO;
import com.web.vo.order.CancelOrderReqVO;
import com.web.vo.order.CancelOrderResVO;
import com.web.vo.order.CreateOrderReqVO;
import com.web.vo.order.CreateOrderResVO;
import com.web.vo.order.GetCancelReasonReqVO;
import com.web.vo.order.GetCancelReasonResVO;
import com.web.vo.order.GetKnightInfoReqVO;
import com.web.vo.order.GetKnightInfoResVO;
import com.web.vo.order.GetOrderDetailReqVO;
import com.web.vo.order.GetOrderDetailResVO;
import com.web.vo.order.PreCancelOrderReqVO;
import com.web.vo.order.PreCancelOrderResVO;
import com.web.vo.order.PreCreateOrderReqVO;
import com.web.vo.order.PreCreateOrderResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/openele")
@Api(tags = "订单管理")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("preCreateOrder")
    @ApiOperation(value = "预下单")
    public PreCreateOrderResVO preCreateOrder(@RequestBody PreCreateOrderReqVO preCreateOrderReqVO) {
        return orderService.preCreateOrder(preCreateOrderReqVO);
    }

    @PostMapping("createOrder")
    @ApiOperation(value = "下单")
    public CreateOrderResVO createOrder(@RequestBody CreateOrderReqVO createOrderReqVO) {
        return orderService.createOrder(createOrderReqVO);
    }

    @PostMapping("getOrderDetail")
    @ApiOperation(value = "查询订单详情")
    public GetOrderDetailResVO getOrderDetail(@RequestBody GetOrderDetailReqVO getOrderDetailReqVO) {
        return orderService.getOrderDetail(getOrderDetailReqVO);
    }

    @PostMapping("preCancelOrder")
    @ApiOperation(value = "预取消")
    public PreCancelOrderResVO preCancelOrder(@RequestBody PreCancelOrderReqVO preCancelOrderReqVO) {
        return orderService.preCancelOrder(preCancelOrderReqVO);
    }

    @PostMapping("cancelOrder")
    @ApiOperation(value = "取消")
    public CancelOrderResVO cancelOrder(@RequestBody CancelOrderReqVO cancelOrderReqVO) {
        return orderService.cancelOrder(cancelOrderReqVO);
    }

    @PostMapping("addTip")
    @ApiOperation(value = "加小费")
    public AddTipResVO addTip(@RequestBody AddTipReqVO addTipReqVO) {
        return orderService.addTip(addTipReqVO);
    }

    @PostMapping("getKnightInfo")
    @ApiOperation(value = "查询骑手信息")
    public GetKnightInfoResVO getKnightInfo(@RequestBody GetKnightInfoReqVO knightInfoReqVO) {
        return orderService.getKnightInfo(knightInfoReqVO);
    }

    @PostMapping("getCancelReasonList")
    @ApiOperation(value = "获取可用取消原因列表")
    public GetCancelReasonResVO getCancelReasonList(@RequestBody GetCancelReasonReqVO cancelReasonReqVO) {
        return orderService.getCancelReasonList(cancelReasonReqVO);
    }
}
