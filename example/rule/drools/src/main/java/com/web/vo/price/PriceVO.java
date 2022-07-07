package com.web.vo.price;

import lombok.Data;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/19
 * @ Description: 描述
 */
@Data
public class PriceVO {

    private String fileName;

    private String name;
    private String province;
    private String city;
    private String county;
    private double money;
}
