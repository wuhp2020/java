package com.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/1
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class InterfaceParamVO {

    private Long id;

    private Long interfaceId;

    private String name;

    private String type;

}
