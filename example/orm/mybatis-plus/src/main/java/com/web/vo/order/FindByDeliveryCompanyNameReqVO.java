package com.web.vo.order;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/3/16
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class FindByDeliveryCompanyNameReqVO {
    private String deliveryCompanyName;
}
