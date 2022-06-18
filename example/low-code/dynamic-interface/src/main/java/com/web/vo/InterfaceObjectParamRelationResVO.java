package com.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/6
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class InterfaceObjectParamRelationResVO {
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

    @ApiModelProperty("接口object字段ID")
    private String interfaceParamId;

    @ApiModelProperty("上级表名")
    private String sourceTableName;

    @ApiModelProperty("上级表字段")
    private String sourceTableColumnName;

    @ApiModelProperty("上级字段类型")
    private String sourceType;


    @ApiModelProperty("当前表名")
    private String targetTableName;

    @ApiModelProperty("当前表名字段")
    private String targetTableColumnName;

    @ApiModelProperty("当前字段类型")
    private String targetType;
}
