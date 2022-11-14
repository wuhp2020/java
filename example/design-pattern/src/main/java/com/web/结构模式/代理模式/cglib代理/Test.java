package com.web.结构模式.代理模式.cglib代理;

import org.springframework.cglib.proxy.Enhancer;

public class Test {

    /**
     * CGLIB创建的动态代理对象比JDK创建的动态代理对象的性能更高，但是CGLIB创建代理对象时所花费的时间却比JDK多得多。
     * 所以对于单例的对象，因为无需频繁创建对象，用CGLIB合适，反之使用JDK方式要更为合适一些。
     * 同时由于CGLib由于是采用动态创建子类的方法，对于final修饰的方法无法进行代理
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test();
        Enhancer enhancer = new Enhancer();
        //设置父类
        enhancer.setSuperclass(Test.class);
        //具体增强
        enhancer.setCallback((org.springframework.cglib.proxy.InvocationHandler) (proxy, method, argss) -> {
            System.out.println("执行前");
            Object returnValue = method.invoke(test, argss);
            System.out.println("执行后");
            return returnValue;
        });
        //动态生成新的目标方法
        Test process = (Test)enhancer.create();
        process.handler();
    }

    public void handler() {
        System.out.println("执行中......");
    }
}
