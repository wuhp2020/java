package com.web.vo.node;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NodeQueryVO {

    @ApiModelProperty(value = "服务名")
    private String serviceName;
}
