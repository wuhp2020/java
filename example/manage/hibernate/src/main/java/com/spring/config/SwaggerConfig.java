package com.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).
                select().apis(RequestHandlerSelectors.any()).
                apis(RequestHandlerSelectors.basePackage("com")).build();
    }

    // swagger标题
    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("hibernate").version("1.0").build();
    }
}
