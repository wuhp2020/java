package com.web.vo.node;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NodeQueryVO {

    @ApiModelProperty(value = "路径")
    private String path;
}
