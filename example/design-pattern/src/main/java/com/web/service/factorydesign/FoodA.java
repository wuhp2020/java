package com.web.service.factorydesign;

import org.springframework.stereotype.Component;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/19
 * @ Description: 描述
 */
@Component
public class FoodA implements Food {

    @Override
    public String foodName() {
        return "A";
    }

    @Override
    public double price() {
        return 1.1;
    }
}
