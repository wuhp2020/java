package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Java11Application {
    public static ApplicationContext applicationContext;
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Java11Application.class, args);
        Java11Application.applicationContext = applicationContext;
    }

    public static ApplicationContext applicationContext() {
        return applicationContext;
    }
}
