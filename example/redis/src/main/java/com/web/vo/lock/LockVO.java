package com.web.vo.lock;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LockVO {

    @ApiModelProperty(value = "锁名称")
    private String key;
}
