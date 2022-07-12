package com.web.vo.store;

import com.web.vo.PageReqVO;
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
public class ChainStoreQueryListReqVO extends PageReqVO {
    @ApiModelProperty(value = "门店id", example = "5313827")
    private String merchant_id;

}
