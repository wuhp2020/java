package com.spring.aspect;

import com.spring.config.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/16
 * @ Desc   : 描述
 */
@Aspect
@Component
@Slf4j
@Order(0)
public class DynamicDBAspect {

    @Pointcut("execution(* com.web.waybill.service.*.*(..))")
    public void dbAspect() {}

    @Before("dbAspect()")
    public void switchDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.setDataSourceKey("private");
        log.info("切换数据源: private, 方法: {}", point.getSignature());
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
