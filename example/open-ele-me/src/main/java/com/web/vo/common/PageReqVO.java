package com.web.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageReqVO {

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page_no;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer page_size;

}
