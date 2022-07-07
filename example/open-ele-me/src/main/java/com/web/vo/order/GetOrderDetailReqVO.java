package com.web.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/27
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class GetOrderDetailReqVO {

    @ApiModelProperty(value = "订单号", example = "")
    private String order_id;

    @ApiModelProperty(value = "外部订单号", example = "1881189236512")
    private String partner_order_code;
}
