package com.web.观察者模式;

import java.util.ArrayList;
import java.util.List;

/**
 * 彩票主题
 */
public class LotterySubject implements Subject {

    /**
     * 所有买彩票的彩民
     */
    private List<Observer> observers = new ArrayList<>();

    /**
     * 开奖号码
     */
    private String msg;

    @Override
    public List<Observer> getObservers() {
        return this.observers;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        int index = observers.indexOf(observer);
        if (index >= 0) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(msg);
        }
    }

    /**
     * 开奖
     */
    public void lottery(String msg) {
        this.msg = msg;
        notifyObservers();
    }
}
