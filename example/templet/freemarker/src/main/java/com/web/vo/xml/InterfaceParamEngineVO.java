package com.web.vo.xml;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InterfaceParamEngineVO {
    @ApiModelProperty("引擎在接口前后置标识")
    private String engineProcessor;

    @ApiModelProperty("引擎类型")
    private String engineType;

    @ApiModelProperty("引擎调用的类")
    private String engineClass;

    @ApiModelProperty("引擎调用的类的方法")
    private String engineMethod;
}
