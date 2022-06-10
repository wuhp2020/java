package com.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * 定义切入点: 对要拦截的方法进行定义与限制, 如包、类
     * 1、execution(public * *(..))任意的公共方法
     *
     * 2、execution(* set*(..))以set开头的所有的方法
     *
     * // com.spring.aspect.Log这个类里的所有的方法
     * 3、execution(* com.spring.aspect.LoggerApply.*(..))
     *
     * // com.spring.aspect包下的所有的类的所有的方法
     * 4、execution(* com.spring.aspect.*.*(..))
     *
     * // com.spring.aspect包及子包下所有的类的所有的方法
     * 5、execution(* com.spring.aspect..*.*(..))
     *
     * // com.spring.aspect包及子包下所有的类的有三个参数,
     * 第一个参数为String类型, 第二个参数为任意类型, 第三个参数为Long类型的方法
     * 6、execution(* com.spring.aspect..*.*(String,?,Long))
     *
     * 7、execution(@annotation(com.spring.aspect.Log))
     */
    @Pointcut("@annotation(com.spring.aspect.Log)")
    private void cutMethod() {

    }

    /**
     * 前置通知：在目标方法执行前调用
     */
    @Before("cutMethod()")
    public void before() {
        log.info("before...");
    }

    /**
     * 后置通知: 在目标方法执行后调用, 若目标方法出现异常, 则不执行
     */
    @AfterReturning("cutMethod()")
    public void afterReturning() {
        log.info("afterReturning...");
    }

    /**
     * 后置/最终通知: 无论目标方法在执行过程中出现一场都会在它之后调用
     */
    @After("cutMethod()")
    public void after() {
        log.info("after");
    }

    /**
     * 异常通知: 目标方法抛出异常时执行
     */
    @AfterThrowing("cutMethod()")
    public void afterThrowing() {
        log.info("afterThrowing...");
    }

    /**
     * 环绕通知：灵活自由的在目标方法中切入代码
     */
    @Around("cutMethod()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标方法的名称
        String methodName = joinPoint.getSignature().getName();
        // 获取方法传入参数
        Object[] params = joinPoint.getArgs();
        Log logg = getDeclaredAnnotation(joinPoint);
        log.info("around...");
        // 执行源方法
        joinPoint.proceed();
        // 模拟进行验证
        if (params != null && params.length > 0 && params[0].equals("Blog Home")) {
            log.info("");
        } else {
            log.info("");
        }
    }

    /**
     * 获取方法中声明的注解
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    public Log getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
        Log annotation = objMethod.getDeclaredAnnotation(Log.class);
        // 返回
        return annotation;
    }
}
