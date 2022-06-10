package com.web.vo.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/27
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class ChainStoreQueryListResVO extends ChainStoreBaseResVO {

    @ApiModelProperty(value = "门店类型: 1-正式门店, 2-测试门店")
    private int chainstore_type;

    @ApiModelProperty(value = "门店类型描述")
    private String chainstore_type_desc;
}
