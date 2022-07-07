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
public class AddTipReqVO {
    @ApiModelProperty(value = "订单号", example = "")
    private String order_id;

    @ApiModelProperty(value = "外部订单号", example = "1881189236512")
    private String partner_order_code;

    @ApiModelProperty(value = "小费金额 分", example = "100")
    private long add_tip_amount_cent;

    @ApiModelProperty(value = "本次加小费唯一标识", example = "1")
    private long third_index_id;
}
