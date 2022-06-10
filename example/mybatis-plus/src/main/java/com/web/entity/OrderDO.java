package com.web.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "order_info")
public class OrderDO {

    @TableId
    private Integer id;

    @TableField("order_no")
    private String orderNo;

    @TableField("take_goods_code")
    private String takeGoodsCode;

    @TableField("channel_source_code")
    private String channelSourceCode;

    @TableField("channel_source_name")
    private String channelSourceName;

    @TableField("business_code")
    private String businessCode;

    @TableField("business_name")
    private String businessName;

    @TableField("business_phone")
    private String businessPhone;

    @TableField("money")
    private Double money;

    @TableField("user_name")
    private String userName;

    @TableField("store_code")
    private String storeCode;

    @TableField("store_name")
    private String storeName;

    @TableField("is_pay")
    private String isPay;

    @TableField("delivery_type")
    private String deliveryType;

    @TableField("delivery_company_code")
    private String deliveryCompanyCode;

    @TableField("delivery_company_name")
    private String deliveryCompanyName;

    @TableField("status")
    private String status;

    @TableField("address")
    private String address;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}