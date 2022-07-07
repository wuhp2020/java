package com.spring.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@Configuration
@Data
public class OpenEleConfig {

    @Value("${open.ele.grant-type}")
    private String grantType;

    @Value("${open.ele.code}")
    private String code;

    @Value("${open.ele.app-id}")
    private String appId;

    @Value("${open.ele.merchant-id}")
    private String merchantId;

    @Value("${open.ele.app-secret}")
    private String appSecret;

    @Value("${open.ele.token-url}")
    private String tokenUrl;

    @Value("${open.ele.order-status-notify-url}")
    private String orderStatusNotifyUrl;

    @Value("${open.ele.pre-create-order-url}")
    private String preCreateOrderUrl;

    @Value("${open.ele.create-order-url}")
    private String createOrderUrl;

    @Value("${open.ele.pre-cancel-order-url}")
    private String preCancelOrderUrl;

    @Value("${open.ele.cancel-order-url}")
    private String cancelOrderUrl;

    @Value("${open.ele.order-detail-url}")
    private String orderDetailUrl;

    @Value("${open.ele.addtip-url}")
    private String addTipUrl;

    @Value("${open.ele.storequery-url}")
    private String storeQueryUrl;

    @Value("${open.ele.storequerylist-url}")
    private String storeQueryListUrl;

    @Value("${open.ele.storecreate-url}")
    private String storeCreateUrl;
}
