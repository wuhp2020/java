package com.web.vo.common;

import com.web.vo.common.Constants;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/28
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class OpenMeiTuanReqVO {
    private String appkey = "test";
    private long timestamp;
    private String version = "1.0";
    private String sign;
}
