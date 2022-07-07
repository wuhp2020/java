package com.web.vo.clone;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/20
 * @ Description: 描述
 */
@Data
@Accessors(chain = true)
public class FruitVO implements Cloneable {

    private String name;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
