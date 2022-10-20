package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.model.ApiInfo;

@SpringBootApplication
public class MagicApiApplication {
    public static void main(String[] args) {
//        ApiInfo apiInfo = new ApiInfo();
//        MagicConfiguration.getMagicResourceService().saveFile(apiInfo);
        SpringApplication.run(MagicApiApplication.class, args);
    }
}
