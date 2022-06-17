package com.web.vo.order;

import lombok.Data;

@Data
public class OrderRequestVO {

    private String userId;

    private String commodityCode;

    private Integer orderCount;
}
