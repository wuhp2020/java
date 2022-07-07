package com.web.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/3/2
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class CreateOrderGoodInfoResVO {

    @ApiModelProperty(value = "服务商品是否支持加小费: 0否, 1是")
    private long can_add_tip;

    @ApiModelProperty(value = "原始配送费总价格(含入参小费金额)")
    private long total_delivery_amount_cent;

    @ApiModelProperty(value = "价格明细 只是展示的加价明细")
    private PriceDetailResVO price_detail;
}
