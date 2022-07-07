package com.web.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderVO {

    @ApiModelProperty(value = "编号")
    private String id;

    @ApiModelProperty(value = "金额")
    private double money;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "地址")
    private String address;
}
