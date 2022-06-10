package com.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value = "model")
public class ModelEntity {

    @TableId
    private Long id;

    @TableField("name")
    private String name;

    @TableField("comment")
    private String comment;

}