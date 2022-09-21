package com.web.观察者模式;

/**
 * 所有订阅者需要实现该接口
 */
public interface Observer {
    void update(String msg);
}

