package com.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/6
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class InterfaceParamReqVO {

    @ApiModelProperty("参数名称")
    private String name;

    @ApiModelProperty("数据类型")
    private String type;

    @ApiModelProperty("参数类型")
    private String paramType;

    @ApiModelProperty("是否必填")
    private boolean required;

    @ApiModelProperty("映射表名")
    private String tableName;

    @ApiModelProperty("映射表名字段名")
    private String tableColumnName;

    @ApiModelProperty("示例值")
    private String sampleValue;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("父级ID")
    private String parentId;

    @ApiModelProperty("子集")
    private List<InterfaceParamReqVO> children;

    @ApiModelProperty("若为object, 需和父级表建立关系")
    private List<InterfaceObjectParamRelationReqVO> relations;
}
