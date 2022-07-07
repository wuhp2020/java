package com.spring.init;

import com.web.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(value = 1)
@Slf4j
public class ResourceInit implements ApplicationRunner {

    @Autowired
    private ResourceService resourceService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        resourceService.initResource();
    }
}
