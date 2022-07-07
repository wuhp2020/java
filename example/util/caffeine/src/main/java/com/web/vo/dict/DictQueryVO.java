package com.web.vo.dict;

import com.web.vo.common.PageQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictQueryVO extends PageQueryVO {

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "中文名")
    private String name;
}
