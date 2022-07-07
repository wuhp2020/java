package com.wuhp.config;

import com.wuhp.bean.ModuleOne;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnBean({Switch.class})
@Slf4j
public class ModuleConfig {

    @Bean
    public ModuleOne moduleOne() {
        log.info("auto config -> com.wuhp.config.ModuleOne");
        return new ModuleOne();
    }
}

