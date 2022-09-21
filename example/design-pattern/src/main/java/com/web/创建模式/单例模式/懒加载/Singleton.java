package com.web.创建模式.单例模式.懒加载;

public class Singleton {
    private static volatile Singleton instance = null;
    private Singleton() {}
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            synchronized (instance) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
