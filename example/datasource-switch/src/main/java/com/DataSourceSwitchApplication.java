package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.web.*.mapper")
@SpringBootApplication
public class DataSourceSwitchApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataSourceSwitchApplication.class, args);
    }
}
