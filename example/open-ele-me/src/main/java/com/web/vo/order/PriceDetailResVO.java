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
public class PriceDetailResVO {

    @ApiModelProperty(value = "起送价")
    private long start_price_cent;

    @ApiModelProperty(value = "距离加价")
    private long distance_price_cent;
}
