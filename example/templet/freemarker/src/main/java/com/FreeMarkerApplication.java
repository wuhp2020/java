package com;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.StringWriter;

@SpringBootApplication
public class FreeMarkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreeMarkerApplication.class, args);
    }
}
