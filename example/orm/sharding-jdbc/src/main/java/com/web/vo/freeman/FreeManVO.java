package com.web.vo.freeman;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FreeManVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "公民名称")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "身份证")
    private String identity;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "家庭住址")
    private String address;

}
