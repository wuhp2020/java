package com.web.vo.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.ssssssss.magicapi.core.model.DataType;

import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/26
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class BaseDefinitionVO {

        @ApiModelProperty(value = "字段名", example = "department")
        private String name;

        @ApiModelProperty(value = "字段值", example = "department")
        private Object value;

        @ApiModelProperty(value = "描述", example = "字段描述")
        private String description;

        @ApiModelProperty(value = "是否必填", example = "true")
        private boolean required;

        @ApiModelProperty(value = "数据类型", example = "Object")
        private DataType dataType;

        @ApiModelProperty(value = "子信息")
        private List<BaseDefinitionVO> children;
}
