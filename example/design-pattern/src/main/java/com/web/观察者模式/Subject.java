package com.web.观察者模式;

import java.util.List;

/**
 * 所有的被观察主题需要实现该接口
 */
public interface Subject {

    /**
     * 存储所有观察者
     * @return List<Observer>
     */
    List<Observer> getObservers();

    /**
     * 注册一个观察者
     * @param observer 订阅的观察者对象
     */
    void registerObserver(Observer observer);

    /**
     * 移除一个观察者
     * @param observer 订阅的观察者对象
     */
    void removeObserver(Observer observer);

    /**
     * 通知所有观察者
     */
    void notifyObservers();
}
