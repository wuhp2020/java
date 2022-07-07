package com.web.vo.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class OpenMeiTuanResVO {
    private String code;
    private String message;
    private String data;
}
