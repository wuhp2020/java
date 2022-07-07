package com.web.vo.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskVO {
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "定时任务完整类名", example = "com.web.service.FaceTask")
    private String cronKey;

    @ApiModelProperty(value = "cron表达式", example = "*/5 * * * * ?")
    private String cronExpression;

    @ApiModelProperty(value = "任务描述")
    private String taskExplain;

    @ApiModelProperty(value = "状态 1:正常, 0:停用")
    private String status;
}
