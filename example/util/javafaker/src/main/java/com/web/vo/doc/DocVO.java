package com.web.vo.doc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class DocVO {
    @ApiModelProperty(value = "编号")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "类型", example = "中间件、框架、操作系统、数据库、语言")
    private List<String> types;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private Date date;
}
