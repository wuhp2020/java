package com.web.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageQueryVO {

    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    @ApiModelProperty(value = "每页数据量")
    private Integer pageSize;

}
