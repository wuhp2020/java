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

    @ApiModelProperty(value = "配送距离: 单位米")
    private long distance;

    @ApiModelProperty(value = "饿了么城市id")
    private int city_id;

    @ApiModelProperty(value = "预询时间戳: 毫秒")
    private long time;

    @ApiModelProperty(value = "服务商品明细")
    private List<CreateOrderGoodInfoResVO> goods_infos;
}
