package com.spring.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class OrderScheduledTask extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("OrderScheduledTask start...");
    }
}
