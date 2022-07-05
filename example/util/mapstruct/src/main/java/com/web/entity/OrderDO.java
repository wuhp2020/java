package com.web.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDO {

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