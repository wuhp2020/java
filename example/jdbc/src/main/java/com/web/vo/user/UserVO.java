package com.web.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVO {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;

}
