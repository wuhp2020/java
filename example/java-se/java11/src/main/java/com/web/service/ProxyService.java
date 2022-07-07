package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Service
@Slf4j
public class ProxyService {

    /**
     * JDK 动态代理
     * 实现原理:
     * 核心逻辑: 通过Proxy.newProxyInstance 生成代理类, 代理类和目标类需要具有相同的接口,
     * 通过实现InvocationHandler接口的invoke方法完成对目标方法的增强操作
     * 优点: 耦合度低, 不需要更改目标类方法完成相应的aop操作
     * 缺点: 目标类必须继承接口, 每次增强的接口只能是一个, 就会造成每个目标类需要对应一个代理类, 较为繁琐
     */
    public void jdkProxy() {
        Process processImp = new ProcessImpl();
        Aop aop = new AopImpl();
        Process process = (Process) Proxy.newProxyInstance(processImp.getClass().getClassLoader(),
                processImp.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        aop.before("wuhp");
                        Object returnValue = method.invoke(processImp, args);
                        aop.after("wuhp");
                        return returnValue;
                    }
                });
        process.handler("执行process");
    }

    /**
     * CGLIB 动态代理
     * CGLIB（Code Generator Library）是一个强大的、高性能的代码生成库, 其被广泛应用于AOP框架（Spring）中, 用以提供方法拦截操作
     * CGLIB代理主要通过对字节码的操作, 以控制对象的访问. CGLIB底层使用了ASM（一个短小精悍的字节码操作框架）来操作字节码生成新的类
     * 实现原理:
     * 核心逻辑: 创建目标类的子类, 通过子类重写目标类方法完成代理
     * 对比: JDK动态代理是面向接口, 在创建代理实现类时比CGLib要快, 创建代理速度快
     * JDK 动态代理要求被代理类实现某个接口, 而 cglib 无该要求
     * CGLib动态代理是通过字节码底层继承要代理类来实现（如果被代理类被final关键字所修饰，那么抱歉会失败）
     * Java动态代理使用Java原生的反射API进行操作（运行期）, 在生成类上比较高效.
     * CGLIB使用ASM框架直接对字节码进行操作（编译期）, 在类的执行过程中比较高效,
     * 简而言之, 创建时jdk动态代理的速度大于cglib, 执行时cglib快于jdk动态代理
     */
    public void cglib() {
        ProcessImpl processImp = new ProcessImpl();
        Enhancer enhancer = new Enhancer();
        //设置父类
        enhancer.setSuperclass(ProcessImpl.class);
        Aop aop = new AopImpl();
        //具体增强
        enhancer.setCallback((org.springframework.cglib.proxy.InvocationHandler) (proxy, method, args) -> {
            aop.before("wuhp");
            Object returnValue = method.invoke(processImp, args);
            aop.after("wuhp");
            return returnValue;
        });
        //动态生成新的目标方法
        Process process = (Process)enhancer.create();
        process.handler("执行process");
    }
}

interface Process {
    String handler(String request);
}
class ProcessImpl implements Process {
    @Override
    public String handler(String request) {
        System.out.println("执行中... 参数: " + request);
        return "执行成功";
    }
}
interface Aop {
    void before(String request);
    void after(String request);
}
class AopImpl implements Aop {
    @Override
    public void before(String request) {
        System.out.println("执行前... 参数: " + request);
    }
    @Override
    public void after(String request) {
        System.out.println("执行后... 参数: " + request);
    }
}