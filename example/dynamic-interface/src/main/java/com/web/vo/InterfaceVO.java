package com.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/31
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class InterfaceVO {

    private String url;

    private String method;

    private String comment;

    private ModelTableColumnVO body;
    private List<InterfaceParamVO> header;
    private List<InterfaceParamVO> params;
    private ModelTableColumnVO result;
}
