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
public class PreCancelOrderResVO {

    @ApiModelProperty(value = "取消实际需扣金额")
    private long actual_cancel_cost_cent;
}
