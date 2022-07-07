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
public class CancelOrderReqVO extends OpenMeiTuanReqVO {
    @ApiModelProperty(value = "订单号")
    private String order_id;

    @ApiModelProperty(value = "外部订单号", example = "14273019930421")
    private String partner_order_code;

    @ApiModelProperty(value = "取消原因", example = "6")
    private int order_cancel_code;

    @ApiModelProperty(value = "取消实际需扣金额", example = "0")
    private long actual_cancel_cost_cent;

    @ApiModelProperty(value = "取消原因补充, 20字以内", example = "物品问题")
    private String order_cancel_other_reason;

    @ApiModelProperty(value = "1-商户取消, 2-用户取消", example = "2")
    private int order_cancel_role;
}
