package com.web.vo.singleton;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/20
 * @ Description: 描述
 */
public class AfterSingletonVO {

    // 首先将 new BeforeSingleton() 堵死
    private AfterSingletonVO() {}

    // 创建私有静态实例
    private static AfterSingletonVO instance = null;

    public static AfterSingletonVO getInstance() {
        if (instance == null) {
            // 加锁
            synchronized (AfterSingletonVO.class) {
                // 这一次判断也是必须的, 不然会有并发问题
                if (instance == null) {
                    instance = new AfterSingletonVO();
                }
            }
        }
        return instance;
    }

}
