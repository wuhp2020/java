package com.spring.task;

import com.spring.config.PropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Slf4j
@Configuration
public class ScheduledConfig {

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        log.info("=====>>>>> " + propertiesConfig.getName() + ", " + propertiesConfig.getAge()
                + ", " + propertiesConfig.getSex() + ", " + propertiesConfig.getAddress());
        log.info("创建定时任务调度线程池 start");
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("task-exec-");
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        log.info("创建定时任务调度线程池 end");
        return threadPoolTaskScheduler;
    }
}
