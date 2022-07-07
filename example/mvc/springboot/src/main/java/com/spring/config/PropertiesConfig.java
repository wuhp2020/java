package com.spring.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:wuhp.properties")
public class PropertiesConfig {

    @Value("${name}")
    private String name;

    @Value("${address}")
    private String address;

    @Value("${sex}")
    private Integer sex;

    @Value("${age}")
    private Integer age;

}
