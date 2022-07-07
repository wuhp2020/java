package com.web.vo.freeman;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FreeManBetchAddVO {

    @ApiModelProperty(value = "起始ID")
    private Long beginId;

    @ApiModelProperty(value = "终止ID")
    private Long endId;
}
