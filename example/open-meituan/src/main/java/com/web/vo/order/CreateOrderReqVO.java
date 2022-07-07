package com.web.vo.order;

import com.web.vo.common.OpenMeiTuanReqVO;
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
public class CreateOrderReqVO extends OpenMeiTuanReqVO {

    @ApiModelProperty(value = "即配送活动标识, 由外部系统生成", example = "14273019930421")
    private long delivery_id;

    @ApiModelProperty(value = "订单id")
    private String order_id;

    @ApiModelProperty(value = "订单来源", example = "201")
    private String outer_order_source_desc;

    @ApiModelProperty(value = "取货门店id", example = "test_0001")
    private String shop_id;

    @ApiModelProperty(value = "配送服务代码)", example = "4012")
    private int delivery_service_code;

    @ApiModelProperty(value = "收件人名称", example = "wuhp")
    private String receiver_name;

    @ApiModelProperty(value = "收件人地址", example = "北京市朝阳区鹏润大厦")
    private String receiver_address;

    @ApiModelProperty(value = "收件人电话", example = "18810027915")
    private String receiver_phone;

    @ApiModelProperty(value = "收件人经度", example = "116398419")
    private int receiver_lng;

    @ApiModelProperty(value = "收件人纬度", example = "39985005")
    private int receiver_lat;

    @ApiModelProperty(value = "货物价格, 单位为元", example = "10")
    private double goods_value;

    @ApiModelProperty(value = "货物重量, 单位为kg", example = "1")
    private int pay_type_code;

}
