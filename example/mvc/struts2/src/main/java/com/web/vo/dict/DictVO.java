package com.web.vo.dict;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictVO {

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "中文名")
    private String name;

    @ApiModelProperty(value = "关联")
    private String relation;

}
