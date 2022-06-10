package com.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value = "interface")
public class InterfaceEntity {

    @TableId
    private Long id;

    @TableField("url")
    private String url;

    @TableField("method")
    private String method;

    @TableField("comment")
    private String comment;

    @TableField("xml")
    private String xml;
}