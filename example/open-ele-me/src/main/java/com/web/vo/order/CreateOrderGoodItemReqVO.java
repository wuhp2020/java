package com.web.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/28
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class CreateOrderGoodItemReqVO {

    @ApiModelProperty(value = "商品编号", example = "1")
    private String item_id;

    @ApiModelProperty(value = "商品名称", example = "鸡翅")
    private String item_name;

    @ApiModelProperty(value = "商品数量", example = "1")
    private int item_quantity;

    @ApiModelProperty(value = "商品原价 分", example = "1000")
    private long item_amount_cent;

    @ApiModelProperty(value = "商品实际支付金额, 必须是乘以数量后的金额, 否则影响售后环节的赔付标准", example = "1000")
    private long item_actual_amount_cent;
}
