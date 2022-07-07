package com.web.vo.dimission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmployeeVO {

    @ApiModelProperty(value = "ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "姓名", example = "wuhp")
    private String name;

    @ApiModelProperty(value = "年龄", example = "28")
    private Integer age;

    @ApiModelProperty(value = "住址", example = "北京")
    private String address;

    @ApiModelProperty(value = "薪资", example = "10000")
    private Double salary;

    @ApiModelProperty(value = "状态", example = "1")
    private String status;
}
