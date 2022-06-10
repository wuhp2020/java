package com.web.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/28
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class OpenEleReqVO {

    private String app_id;
    private String merchant_id;
    private String timestamp;
    private String version = "1.0";
    private String business_data;
    private String signature;
    private String access_token;

}
