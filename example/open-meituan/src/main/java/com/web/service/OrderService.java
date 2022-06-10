package com.web.service;

import com.spring.config.OpenMeiTuanUrlConfig;
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
    private OpenMeiTuanUrlConfig openMeiTuanUrlConfig;

    @Autowired
    private OpenMeiTuanClient openMeiTuanClient;

    public PreCreateOrderResVO preCreateOrder(PreCreateOrderReqVO preCreateOrderReqVO) {
        PreCreateOrderResVO orderResVO = openMeiTuanClient.httpPost(
                openMeiTuanUrlConfig.getPreCreateOrderUrl(), preCreateOrderReqVO, PreCreateOrderResVO.class);
        return orderResVO;
    }

    public CreateOrderResVO createOrder(CreateOrderReqVO createOrderReqVO) {
        CreateOrderResVO orderResVO = openMeiTuanClient.httpPost(
                openMeiTuanUrlConfig.getCreateOrderUrl(), createOrderReqVO, CreateOrderResVO.class);
        return orderResVO;
    }

    public GetOrderDetailResVO getOrderDetail(GetOrderDetailReqVO getOrderDetailReqVO) {
        GetOrderDetailResVO orderResVO = openMeiTuanClient.httpPost(
                openMeiTuanUrlConfig.getOrderDetailUrl(), getOrderDetailReqVO, GetOrderDetailResVO.class);
        return orderResVO;
    }

    public PreCancelOrderResVO preCancelOrder(PreCancelOrderReqVO preCancelOrderReqVO) {
        PreCancelOrderResVO orderResVO = openMeiTuanClient.httpPost(
                openMeiTuanUrlConfig.getPreCancelOrderUrl(), preCancelOrderReqVO, PreCancelOrderResVO.class);
        return orderResVO;
    }

    public CancelOrderResVO cancelOrder(CancelOrderReqVO cancelOrderReqVO) {
        CancelOrderResVO orderResVO = openMeiTuanClient.httpPost(
                openMeiTuanUrlConfig.getCancelOrderUrl(), cancelOrderReqVO, CancelOrderResVO.class);
        return orderResVO;
    }

    public AddTipResVO addTip(AddTipReqVO addTipReqVO) {
        AddTipResVO addTipResVO = openMeiTuanClient.httpPost(
                openMeiTuanUrlConfig.getAddTipUrl(), addTipReqVO, AddTipResVO.class);
        return addTipResVO;
    }

    public GetKnightInfoResVO getKnightInfo(GetKnightInfoReqVO knightInfoReqVO) {
        GetKnightInfoResVO knightInfoResVO = openMeiTuanClient.httpPost(
                openMeiTuanUrlConfig.getCancelOrderUrl(), knightInfoReqVO, GetKnightInfoResVO.class);
        return knightInfoResVO;
    }

    public GetCancelReasonResVO getCancelReasonList(GetCancelReasonReqVO cancelReasonReqVO) {
        GetCancelReasonResVO cancelReasonResVO = openMeiTuanClient.httpPost(
                openMeiTuanUrlConfig.getCancelOrderUrl(), cancelReasonReqVO, GetCancelReasonResVO.class);
        return cancelReasonResVO;
    }
}
