package com.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/6
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class InterfaceResVO {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("创建人id")
    private String createUserId;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyDate;

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
    private List<InterfaceParamResVO> params = new ArrayList<>();

    @ApiModelProperty("body参数")
    private InterfaceParamResVO body = new InterfaceParamResVO();

    @ApiModelProperty("header参数")
    private List<InterfaceParamResVO> header = new ArrayList<>();

    @ApiModelProperty("cookie参数")
    private List<InterfaceParamResVO> cookie = new ArrayList<>();

    @ApiModelProperty("auth")
    private List<InterfaceParamResVO> auth = new ArrayList<>();

    @ApiModelProperty("返回值")
    private InterfaceParamResVO result = new InterfaceParamResVO();


}
