package com.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value = "model_table")
public class ModelTableEntity {

    @TableId
    private Long id;

    @TableField("model_id")
    private Long modelId;

    @TableField("name")
    private String name;

    @TableField("comment")
    private String comment;

    @TableField("parent_id")
    private Long parentId;

}