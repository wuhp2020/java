package com.web.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_interface")
public class InterfaceEntity {

    @TableId
    private String id;

    @TableField("item")
    private String item;

    @TableField("name")
    private String name;

    @TableField("method")
    private String method;

    @TableField("url")
    private String url;

    @TableField("status")
    private String status;

    @TableField("responsible")
    private String responsible;

    @TableField("tag")
    private String tag;

    @TableField("remark")
    private String remark;

    @TableField("create_user_id")
    private String createUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "modify_date", fill = FieldFill.INSERT_UPDATE)
    private Date modifyDate;
}
