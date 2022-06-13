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
@TableName("sys_interface_object_param_relation")
public class InterfaceObjectParamRelationEntity {

    @TableId
    private String id;

    @TableField("interface_id")
    private String interfaceId;

    @TableField("interface_param_id")
    private String interfaceParamId;

    @TableField("source_table_name")
    private String sourceTableName;

    @TableField("source_table_column_name")
    private String sourceTableColumnName;

    @TableField("source_type")
    private String sourceType;


    @TableField("target_table_name")
    private String targetTableName;

    @TableField("target_table_column_name")
    private String targetTableColumnName;

    @TableField("target_type")
    private String targetType;

    @TableField("create_user_id")
    private String createUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "modify_date", fill = FieldFill.INSERT_UPDATE)
    private Date modifyDate;
}
