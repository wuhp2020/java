package com.web.vo.node;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NodeVO {
    @ApiModelProperty(value = "key")
    private String key;

    @ApiModelProperty(value = "value")
    private String value;
}
