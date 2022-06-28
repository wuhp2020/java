package com.web.vo.xml;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class InterfaceParamVO {

    @ApiModelProperty("参数名称")
    private String javaName;

    @ApiModelProperty("数据类型")
    private String javaType;

    @ApiModelProperty("映射表名")
    private String tableName;

    @ApiModelProperty("映射表名字段名")
    private String tableColumnName;

    @ApiModelProperty("表字段类型")
    private String tableColumnType;

    @ApiModelProperty("是否有引擎")
    private boolean haveEngine;

    @ApiModelProperty("引擎集合")
    private List<InterfaceParamEngineVO> engines;

    @ApiModelProperty("子集")
    private List<InterfaceParamVO> children = new ArrayList<>();

    @ApiModelProperty("object需和父级表建立关系")
    private List<InterfaceObjectParamRelationVO> relations;
}
