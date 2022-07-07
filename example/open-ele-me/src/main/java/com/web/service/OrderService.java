package com.web.service;

import com.spring.config.OpenEleConfig;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@Service
@Slf4j
public class OrderService {

    @Autowired
    private OpenEleConfig openEleUrlConfig;

    @Autowired
    private OpenEleClient openEleClient;

    public PreCreateOrderResVO preCreateOrder(PreCreateOrderReqVO preCreateOrderReqVO) {
        PreCreateOrderResVO orderResVO = openEleClient.httpPost(
                openEleUrlConfig.getPreCreateOrderUrl(), preCreateOrderReqVO, PreCreateOrderResVO.class);
        return orderResVO;
    }

    public CreateOrderResVO createOrder(CreateOrderReqVO createOrderReqVO) {
        CreateOrderResVO orderResVO = openEleClient.httpPost(
                openEleUrlConfig.getCreateOrderUrl(), createOrderReqVO, CreateOrderResVO.class);
        return orderResVO;
    }

    public GetOrderDetailResVO getOrderDetail(GetOrderDetailReqVO getOrderDetailReqVO) {
        GetOrderDetailResVO orderResVO = openEleClient.httpPost(
                openEleUrlConfig.getOrderDetailUrl(), getOrderDetailReqVO, GetOrderDetailResVO.class);
        return orderResVO;
    }

    public PreCancelOrderResVO preCancelOrder(PreCancelOrderReqVO preCancelOrderReqVO) {
        PreCancelOrderResVO orderResVO = openEleClient.httpPost(
                openEleUrlConfig.getPreCancelOrderUrl(), preCancelOrderReqVO, PreCancelOrderResVO.class);
        return orderResVO;
    }

    public CancelOrderResVO cancelOrder(CancelOrderReqVO cancelOrderReqVO) {
        CancelOrderResVO orderResVO = openEleClient.httpPost(
                openEleUrlConfig.getCancelOrderUrl(), cancelOrderReqVO, CancelOrderResVO.class);
        return orderResVO;
    }

    public AddTipResVO addTip(AddTipReqVO addTipReqVO) {
        AddTipResVO addTipResVO = openEleClient.httpPost(
                openEleUrlConfig.getAddTipUrl(), addTipReqVO, AddTipResVO.class);
        return addTipResVO;
    }

    public GetKnightInfoResVO getKnightInfo(GetKnightInfoReqVO knightInfoReqVO) {
        GetKnightInfoResVO knightInfoResVO = openEleClient.httpPost(
                openEleUrlConfig.getCancelOrderUrl(), knightInfoReqVO, GetKnightInfoResVO.class);
        return knightInfoResVO;
    }

    public GetCancelReasonResVO getCancelReasonList(GetCancelReasonReqVO cancelReasonReqVO) {
        GetCancelReasonResVO cancelReasonResVO = openEleClient.httpPost(
                openEleUrlConfig.getCancelOrderUrl(), cancelReasonReqVO, GetCancelReasonResVO.class);
        return cancelReasonResVO;
    }
}
