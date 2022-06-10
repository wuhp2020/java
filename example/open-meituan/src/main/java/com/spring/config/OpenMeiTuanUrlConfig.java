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
public class OpenMeiTuanUrlConfig {

    @Value("${open.meituan.order-status-notify-url}")
    private String orderStatusNotifyUrl;

    @Value("${open.meituan.pre-create-order-url}")
    private String preCreateOrderUrl;

    @Value("${open.meituan.create-order-url}")
    private String createOrderUrl;

    @Value("${open.meituan.pre-cancel-order-url}")
    private String preCancelOrderUrl;

    @Value("${open.meituan.cancel-order-url}")
    private String cancelOrderUrl;

    @Value("${open.meituan.order-detail-url}")
    private String orderDetailUrl;

    @Value("${open.meituan.addtip-url}")
    private String addTipUrl;
}
