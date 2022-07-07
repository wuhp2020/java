package com.spring.config;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Struts2Config {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new StrutsPrepareAndExecuteFilter());
        List list = new ArrayList();
        list.add("/api/v1/*");
        filterRegistrationBean.setUrlPatterns(list);
        return filterRegistrationBean;
    }
}
