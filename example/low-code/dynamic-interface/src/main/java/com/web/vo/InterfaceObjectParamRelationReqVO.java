package com.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/6
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class InterfaceObjectParamRelationReqVO {

    @ApiModelProperty("上级表名")
    private String sourceTableName;

    @ApiModelProperty("上级表字段")
    private String sourceTableColumnName;

    @ApiModelProperty("当前表名")
    private String targetTableName;

    @ApiModelProperty("当前表名字段")
    private String targetTableColumnName;
}
