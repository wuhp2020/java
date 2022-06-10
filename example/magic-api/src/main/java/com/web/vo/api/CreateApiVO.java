package com.web.vo.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Collections;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/26
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class CreateApiVO {

    @ApiModelProperty(value = "方法", example = "POST")
    private String method;

    @ApiModelProperty(value = "路径", example = "create")
    private String path;

    @ApiModelProperty(value = "脚本")
    private String script;

    @ApiModelProperty(value = "组ID", example = "86dcccfa24134c0d875ea345d867cbd0")
    private String groupId;

    @ApiModelProperty(value = "接口名称", example = "创建")
    private String name;

    @ApiModelProperty(value = "设置的请求参数")
    private List<ParameterVO> parameters = Collections.emptyList();

    @ApiModelProperty(value = "请求体", example = "\"{\"id\": 1, \"name\": \"发展部\"}\"")
    private String requestBody;

    @ApiModelProperty(value = "请求头")
    private List<HeaderVO> headers = Collections.emptyList();

    @ApiModelProperty(value = "输出结果", example = "\"{\"code\": 200, \"message\": \"ok\"}\"")
    private String responseBody;

    @ApiModelProperty(value = "接口描述", example = "创建")
    private String description;

    @ApiModelProperty(value = "请求体属性")
    private BaseDefinitionVO requestBodyDefinition;

    @ApiModelProperty(value = "输出结果属性")
    private BaseDefinitionVO responseBodyDefinition;
}
