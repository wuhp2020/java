package com.spring.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.interceptor.RequestInterceptor;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.script.MagicScriptContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/27
 * @ Desc   : 自定义拦截器
 */
@Slf4j
@Order(1)
@Component
public class MagicApiRequestInterceptor implements RequestInterceptor {
    /*
     * 当返回对象时, 直接将此对象返回到页面, 返回null时, 继续执行后续操作
     */
    @Override
    public Object preHandle(ApiInfo info, MagicScriptContext context, HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("请求前: " + info.getPath());
        // 拦截请求并直接返回结果, 不继续后续代码
        // 需要注意的是, 拦截器返回的结果不会被包裹一层json值, 也不会走ResultProvider
        // return new JsonBean<>(100,"拦截器返回");
        // 放开请求, 执行后续代码
        return null;
    }

    /**
     * 执行完毕之后执行
     * @param value 即将要返回到页面的值
     * @return 返回到页面的对象, 当返回null时执行后续拦截器, 否则直接返回该值, 不执行后续拦截器
     */
    @Override
    public Object postHandle(ApiInfo info, MagicScriptContext context, Object value, HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("请求后: " + info.getPath());
//        System.out.println("返回结果: " + value);
        // 拦截请求并直接返回结果, 不继续后续代码
        // 需要注意的是, 拦截器返回的结果不会被包裹一层json值, 也不会走ResultProvider
        // return new JsonBean<>(100,"拦截器返回");
        // 放开请求, 执行后续代码
        return null;
    }
}
