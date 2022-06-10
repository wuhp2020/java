package com.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/1
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class ModelTableColumnVO {

    @ApiModelProperty(value = "ID")
    private Long id;

    private Long modelId;

    private Long tableId;

    @ApiModelProperty(value = "字段名")
    private String name;

    @ApiModelProperty(value = "字段说明")
    private String comment;

    @ApiModelProperty(value = "字段类型")
    private String type;

    @ApiModelProperty(value = "字段长度")
    private String length;

    @ApiModelProperty(value = "子表")
    private List<ModelTableColumnVO> children;

    @ApiModelProperty(value = "父ID")
    private Long parentId;
}
