package com.web.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/3/3
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class GetOrderEventLogDetailResVO {

    @ApiModelProperty(value = "订单状态: 0-订单生成, 1-运单生成成功, 20-骑手接单, 80-骑手到店, 2-配送中, 3-已完成, 4-已取消, 5-配送异常")
    private int order_status;

    @ApiModelProperty(value = "发生时间")
    private long occur_time;

    @ApiModelProperty(value = "配送员姓名")
    private String carrier_driver_name;

    @ApiModelProperty(value = "配送员电话")
    private String carrier_driver_phone;

}
