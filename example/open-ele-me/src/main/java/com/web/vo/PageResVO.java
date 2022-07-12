package com.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ Author     : wuheping
 * @ Date       : 2022/7/12
 * @ Description: 描述
 */
@Data
public class PageResVO<T> {

    @ApiModelProperty(value = "页码")
    private Integer page_no;

    @ApiModelProperty(value = "每页数据量")
    private Integer page_size;

    @ApiModelProperty(value = "总页数")
    private Integer total_page;

    @ApiModelProperty(value = "总数据量")
    private Integer total_count;

    private T list;

}
