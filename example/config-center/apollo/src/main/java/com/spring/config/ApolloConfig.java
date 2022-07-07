package com.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/10
 * @ Desc   : 描述
 */
@Configuration
public class ApolloConfig {
    @Value("${v1:defaultValue}")
    public String v1;
}
