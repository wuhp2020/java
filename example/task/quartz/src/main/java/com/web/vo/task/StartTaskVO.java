package com.web.vo.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StartTaskVO {
    @ApiModelProperty(value = "启动类", example = "com.spring.task.GoodsScheduledTask")
    private String jobClass;

    @ApiModelProperty(value = "任务名称", example = "test")
    private String jobName;

    @ApiModelProperty(value = "任务组名", example = "test")
    private String jobGroupName;

    @ApiModelProperty(value = "任务时间", example = "0/1 * * * * ?")
    private String jobTime;
}
