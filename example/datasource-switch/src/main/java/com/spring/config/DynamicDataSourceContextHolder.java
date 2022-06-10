package com.spring.config;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/16
 * @ Desc   : 描述
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal();


    public static synchronized void setDataSourceKey(String key) {
        contextHolder.set(key);
    }

    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    public static void clearDataSourceKey() {
        contextHolder.remove();
    }
}
