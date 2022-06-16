package com.web.vo.alert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AlertVO {

    @ApiModelProperty(value = "消息")
    private String message;
}
