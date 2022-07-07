package com.web.vo.xml;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InterfaceObjectParamRelationVO {
    @ApiModelProperty("上级表名")
    private String sourceTableName;

    @ApiModelProperty("上级表字段")
    private String sourceTableColumnName;

    @ApiModelProperty("上级字段类型")
    private String sourceTableColumnType;


    @ApiModelProperty("当前表名")
    private String targetTableName;

    @ApiModelProperty("当前表名字段")
    private String targetTableColumnName;

    @ApiModelProperty("当前字段类型")
    private String targetTableColumnType;
}
