package com.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@Slf4j
public class FilterConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        // 添加过滤器
        registration.setFilter(new MyFilter());
        // 设置过滤路径, /*所有路径
        registration.addUrlPatterns("/*");
        //添加默认参数
        registration.addInitParameter("name", "alue");
        // 设置优先级
        registration.setName("MyFilter");
        //设置优先级
        registration.setOrder(1);
        return registration;
    }

    public class MyFilter implements Filter {

        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;

            //打印请求Url
            log.info("myfilter doing, url: " + request.getRequestURI());
            filterChain.doFilter(servletRequest, servletResponse);
        }

        @Override
        public void destroy() {

        }
    }
}
