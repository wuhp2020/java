package com.web.vo.node;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NodeQueryVO {

    @ApiModelProperty(value = "key")
    private String key;
}
