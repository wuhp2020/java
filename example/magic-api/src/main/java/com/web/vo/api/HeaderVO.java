package com.web.vo.api;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/26
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class HeaderVO {

    private String name;
    private String value;
}
