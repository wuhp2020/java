package com.web.vo.device;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceVO {
    @ApiModelProperty(value = "设备id")
    private String id;

    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "生产日期")
    private Date createDate;
}
