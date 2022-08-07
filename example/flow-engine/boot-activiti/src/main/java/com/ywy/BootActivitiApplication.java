package com.ywy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ywy"})
public class BootActivitiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootActivitiApplication.class, args);
    }

}
