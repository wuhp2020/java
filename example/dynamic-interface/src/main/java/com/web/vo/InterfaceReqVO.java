package com.web.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/6
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class InterfaceReqVO {

    @ApiModelProperty("项目")
    private String item;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("方法类型")
    private String method;

    @ApiModelProperty("路径")
    private String url;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("责任人")
    private String responsible;

    @ApiModelProperty("标签")
    private String tag;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("params参数集合")
    private List<InterfaceParamReqVO> params;

    @ApiModelProperty("body参数")
    private InterfaceParamReqVO body;

    @ApiModelProperty("header参数")
    private List<InterfaceParamReqVO> header;

    @ApiModelProperty("cookie参数")
    private List<InterfaceParamReqVO> cookie;

    @ApiModelProperty("auth")
    private List<InterfaceParamReqVO> auth;

    @ApiModelProperty("返回值")
    private InterfaceParamReqVO result;


}
