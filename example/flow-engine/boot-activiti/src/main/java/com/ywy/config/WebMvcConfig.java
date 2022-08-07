package com.ywy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-09 10:15
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // bpmn文件上传路径映射
        registry.addResourceHandler("/bpmn/**").addResourceLocations("file:" + GlobalConfig.bpmnFileUploadPath);
    }
}
