package com.web.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/31
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class ModelVO {

    private Long id;

    private String name;

    private String comment;

    private ModelTableColumnVO column;
}
