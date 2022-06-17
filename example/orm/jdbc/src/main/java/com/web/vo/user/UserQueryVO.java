package com.web.vo.user;

import com.web.vo.common.PageQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserQueryVO extends PageQueryVO {

    @ApiModelProperty(value = "用户名")
    private String username;
}
