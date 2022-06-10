package com.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value = "interface_param")
public class InterfaceParamEntity {
    @TableId
    private Long id;

    @TableField("interface_id")
    private Long interfaceId;

    @TableField("name")
    private String name;

    @TableField("type")
    private String type;

    @TableField("model_id")
    private Long modelId;

    @TableField("table_id")
    private Long tableId;
}