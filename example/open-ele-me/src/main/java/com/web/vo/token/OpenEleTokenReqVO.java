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
public class OpenEleTokenReqVO {
    private String grant_type;
    private String code;
    private String app_id;
    private String merchant_id;
    private String signature;
    private String timestamp;
}
