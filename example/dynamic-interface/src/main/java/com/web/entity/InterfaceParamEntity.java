package com.web.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/6
 * @ Desc   : 描述
 */
@Data
@TableName("sys_interface_param")
public class InterfaceParamEntity {

    @TableId
    private String id;

    @TableField("interface_id")
    private String interfaceId;

    @TableField("name")
    private String name;

    @TableField("type")
    private String type;

    @TableField("param_type")
    private String paramType;

    @TableField("required")
    private boolean required;

    @TableField("table_name")
    private String tableName;

    @TableField("table_column_name")
    private String tableColumnName;

    @TableField("sample_value")
    private String sampleValue;

    @TableField("remark")
    private String remark;

    @TableField("parent_id")
    private String parentId;

    @TableField("create_user_id")
    private String createUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "modify_date", fill = FieldFill.INSERT_UPDATE)
    private Date modifyDate;

}
