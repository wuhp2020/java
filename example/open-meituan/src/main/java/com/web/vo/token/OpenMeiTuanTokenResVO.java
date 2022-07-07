package com.web.vo.token;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class OpenMeiTuanTokenResVO {
    private String app_id;
    private String merchant_id;
    private String access_token;
    private String refresh_token;
    private Long expire_in;
    private Long re_expire_in;
}
