package com.web.结构模式.适配器模式.接口适配器模式;

import com.web.结构模式.适配器模式.对象适配器模式.VGA;

public class Projector<T> {
    public void projection(T t) {
        if (t instanceof VGA) {
            System.out.println("开始投影");
            VGA v = (VGA) t;
            v.projection();
        } else {
            System.out.println("接口不匹配，无法投影");
        }
    }
}
