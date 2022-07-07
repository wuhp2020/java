package com.web.vo.store;

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
public class ChainStoreQueryReqVO {

    @ApiModelProperty(value = "门店id", example = "5313827")
    private String merchant_id;

    @ApiModelProperty(value = "门店id", example = "209191139")
    private long chain_store_id;

}
