package com.web.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HystrixService {

    @HystrixCommand(fallbackMethod = "lowerOne")
    public String lowerGrade() {
        throw new RuntimeException("lowerGrade error");
    }

    @HystrixCommand(fallbackMethod = "lowerTwo")
    public String lowerOne() {
        throw new RuntimeException("lowerOne error");
    }

    @HystrixCommand(defaultFallback = "lowerDefault")
    public String lowerTwo() {
        throw new RuntimeException("lowerTwo error");
    }

    public String lowerDefault() {
        return "降级到Default";
    }
}
