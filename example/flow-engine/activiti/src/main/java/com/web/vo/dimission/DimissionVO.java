package com.web.vo.dimission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DimissionVO {

    private EmployeeVO employeeVO;

    @ApiModelProperty(value = "是否结束", example = "1")
    private String isEnd;

    @ApiModelProperty(value = "任务ID", example = "11")
    private String taskId;
}
