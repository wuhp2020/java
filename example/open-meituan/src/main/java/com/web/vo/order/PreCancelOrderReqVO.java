package com.web.vo.order;

import com.web.vo.common.OpenMeiTuanReqVO;
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
public class PreCancelOrderReqVO extends OpenMeiTuanReqVO {
    @ApiModelProperty(value = "订单号")
    private String order_id;

    @ApiModelProperty(value = "外部订单号", example = "14273019930421")
    private String partner_order_code;

    @ApiModelProperty(value = "取消原因", example = "6")
    private int order_cancel_code;
}
