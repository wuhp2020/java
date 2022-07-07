package com.web.vo.node;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NodeVO {



    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "节点数据")
    private String data;
}
