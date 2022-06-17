package com.web.vo.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusinessRequestVO {
    @ApiModelProperty(value = "用户ID")
    String userId;

    @ApiModelProperty(value = "商品编号")
    String commodityCode;

    @ApiModelProperty(value = "订单编号")
    int orderCount;
}
