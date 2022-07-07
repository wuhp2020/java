package com.web.waybill.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "waybill_info")
public class WaybillDO {

    @TableId
    private Integer id;

    @TableField("order_no")
    private String orderNo;

    @TableField("waybill_no")
    private String waybillNo;

    @TableField("delivery_fee")
    private double deliveryFee;

    @TableField("rider_name")
    private String riderName;

    @TableField("delivery_type")
    private String deliveryType;

    @TableField("delivery_company_code")
    private String deliveryCompanyCode;

    @TableField("delivery_company_name")
    private String deliveryCompanyName;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private Date createTime;
}