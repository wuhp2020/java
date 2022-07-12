package com.web.vo;

/**
 * @ Author     : wuheping
 * @ Date       : 2022/7/12
 * @ Description: 描述
 */

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class OpenEleResVO {
    private String sign;
    private String code;
    private String msg;
    private String business_data;
}

