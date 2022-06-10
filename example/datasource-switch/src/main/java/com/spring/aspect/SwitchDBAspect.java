package com.spring.aspect;

import com.spring.config.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/16
 * @ Desc   : 描述
 */
@Aspect
@Component
@Slf4j
@Order(0)
public class SwitchDBAspect {

    @Pointcut("@annotation(com.spring.aspect.SwitchDB)")
    public void dbAspect() {}

    @Before("dbAspect()")
    public void switchDataSource(JoinPoint joinPoint) throws Exception {
        //1. 获取目标对象对应的字节码对象
        Class<?> targetCls = joinPoint.getTarget().getClass();

        //2获取目标方法对象
        //获取方法签名信息从而获取方法名和参数类型
        Signature signature = joinPoint.getSignature();
        //将方法签名强转成MethodSignature类型，方便调用
        MethodSignature ms = (MethodSignature)signature;
        //通过字节码对象以及方法签名获取目标方法对象
        Method targetMethod = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());


        //3获取目标方法对象上注解中的属性值
        //获取方法上的自定义requiredLog注解
        SwitchDB switchDB = targetMethod.getAnnotation(SwitchDB.class);

        //1.2.4 获取自定义注解中operation属性的值
        String value = switchDB.value();
        DynamicDataSourceContextHolder.setDataSourceKey(value);
        log.info("切换数据源: private, 方法: {}", joinPoint.getSignature());
    }

    @Around("dbAspect()")
    public Object execDataSource(ProceedingJoinPoint point) throws Throwable {
        return point.proceed();
    }

    @After("dbAspect())")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        log.info("重置数据源: default, 方法: {}", point.getSignature());
    }
}
