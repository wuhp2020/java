package com.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/6
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class InterfaceParamResVO {
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("创建人id")
    private String createUserId;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyDate;

    @ApiModelProperty("接口ID")
    private String interfaceId;

    @ApiModelProperty("参数名称")
    private String name;

    @ApiModelProperty("参数类型")
    private String paramType;

    @ApiModelProperty("数据类型")
    private String type;

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
    private List<InterfaceParamResVO> children = new ArrayList<>();

    @ApiModelProperty("若为object, 需和父级表建立关系")
    private List<InterfaceObjectParamRelationResVO> relations;
}
