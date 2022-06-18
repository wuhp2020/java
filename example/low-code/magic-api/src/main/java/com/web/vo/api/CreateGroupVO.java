package com.web.vo.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/26
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class CreateGroupVO {

    @ApiModelProperty(value = "名称", example = "部门管理")
    private String name;

    @ApiModelProperty(value = "类型", example = "api")
    private String type;

    @ApiModelProperty(value = "父ID", example = "0")
    private String parentId;

    @ApiModelProperty(value = "路径", example = "/api/v1/department")
    private String path;
}
