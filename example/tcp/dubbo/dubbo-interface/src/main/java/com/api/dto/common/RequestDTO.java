package com.api.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ Author : wuheping
 * @ Date   : 2022/4/29
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class RequestDTO<T> implements Serializable {

    @ApiModelProperty(value = "链路码")
    private String reqNo;

    @ApiModelProperty(value = "数据")
    private T body;

}
