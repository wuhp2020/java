package com.web.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/27
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class CreateOrderReqVO {

    @ApiModelProperty(value = "外部订单号", example = "1881189236512")
    private String partner_order_code;

    @ApiModelProperty(value = "取货经度", example = "116.31")
    private double transport_longitude;

    @ApiModelProperty(value = "取货纬度", example = "40.04")
    private double transport_latitude;

    @ApiModelProperty(value = "取货点联系人电话", example = "18811892365")
    private String transport_tel;

    @ApiModelProperty(value = "门店id", example = "209191139")
    private long chain_store_id;

    @ApiModelProperty(value = "订单类型(1:即时单, 3:预约单)", example = "1")
    private String order_type;

    @ApiModelProperty(value = "经纬度来源 1:腾讯地图, 2:百度地图, 3:高德地图", example = "2")
    private int position_source;

    @ApiModelProperty(value = "收货人地址", example = "北京市朝阳区鹏润大厦")
    private String receiver_address;

    @ApiModelProperty(value = "收货人经度", example = "116.31")
    private double receiver_longitude;

    @ApiModelProperty(value = "收货人纬度", example = "40.04")
    private double receiver_latitude;

    @ApiModelProperty(value = "订单商品总金额 分", example = "1000")
    private long goods_total_amount_cent;

    @ApiModelProperty(value = "订单商品客户实际支付金额 分", example = "1000")
    private long goods_actual_amount_cent;

    @ApiModelProperty(value = "订单小费 分", example = "0")
    private long order_tip_amount_cent;

    @ApiModelProperty(value = "订单总重量 kg", example = "1.2")
    private double goods_weight;

    @ApiModelProperty(value = "货物件数", example = "1")
    private int goods_count;

    @ApiModelProperty(value = "收货人姓名", example = "wuhp")
    private String receiver_name;

    @ApiModelProperty(value = "收货人主要联系方式", example = "18811892365")
    private String receiver_primary_phone;

    @ApiModelProperty(value = "货物明细")
    private List<CreateOrderGoodItemReqVO> goods_item_list;

}
