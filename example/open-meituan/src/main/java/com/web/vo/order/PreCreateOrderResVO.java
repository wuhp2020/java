package com.web.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class PreCreateOrderResVO {
    @ApiModelProperty(value = "订单配送价格, 单位为元")
    private double delivery_fee;
}
