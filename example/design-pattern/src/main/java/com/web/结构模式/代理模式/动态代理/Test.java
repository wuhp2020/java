package com.web.结构模式.代理模式.动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test implements AOP {

    /**
     * 1.代理对象,不需要实现接口
     * 2.代理对象的生成,是利用JDK的API,动态的在内存中构建代理对象(需要我们指定创建代理对象/目标对象实现的接口的类型)
     * 代理类不用再实现接口了。但是，要求被代理对象必须有接口。
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test();
        AOP aop = (AOP) Proxy.newProxyInstance(test.getClass().getClassLoader(),
                test.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("执行前");
                        Object returnValue = method.invoke(test, args);
                        System.out.println("执行后");
                        return returnValue;
                    }
                });

        aop.proxy();

    }

    @Override
    public void proxy() {
        System.out.println("执行中......");
    }
}
