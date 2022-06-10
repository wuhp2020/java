package com.web.vo.operationlog;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
public class OperationLogSetupVO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "保留天数")
    private Integer days;

}
