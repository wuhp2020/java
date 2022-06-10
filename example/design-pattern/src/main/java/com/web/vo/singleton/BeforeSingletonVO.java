package com.web.vo.singleton;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/20
 * @ Description: 描述
 */
public class BeforeSingletonVO {


    // 首先将 new BeforeSingleton() 堵死
    private BeforeSingletonVO() {}

    // 创建私有静态实例, 意味着这个类第一次使用的时候就会进行创建
    private static BeforeSingletonVO instance = new BeforeSingletonVO();

    public static BeforeSingletonVO getInstance() {
        return instance;
    }


}
