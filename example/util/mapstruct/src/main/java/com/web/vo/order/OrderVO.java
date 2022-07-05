package com.web.vo.order;

import lombok.Data;

import java.util.Date;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/6
 * @ Desc   : 描述
 */
@Data
public class OrderVO {

    private Integer id;

    private String orderNo;

    private String takeGoodsCode;

    private String channelSourceCode;

    private String channelSourceName;

    private String businessCode;

    private String businessName;

    private String businessPhone;

    private double money;

    private String userName;

    private String storeCode;

    private String storeName;

    private String isPay;

    private String deliveryType;

    private String deliveryCompanyCode;

    private String deliveryCompanyName;

    private String status;

    private String address;

    private Date createTime;
}
