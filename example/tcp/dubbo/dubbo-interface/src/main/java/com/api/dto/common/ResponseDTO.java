package com.api.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ResponseDTO<T> implements Serializable {

    @ApiModelProperty(value = "状态")
    private String code;

    @ApiModelProperty(value = "描述")
    private String message;

    @ApiModelProperty(value = "数据")
    private T body;

}

