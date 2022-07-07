package com.web.service;

import com.google.common.collect.Maps;
import com.web.vo.task.TaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private static Map<String, TaskVO> taskMap = Maps.newConcurrentMap();

    private static Map<String, ScheduledFuture> scheduledFutureMap = Maps.newConcurrentMap();

    public void create(TaskVO taskVO) throws Exception {
        taskMap.put(taskVO.getId(), taskVO);
    }

    public void start(String id) throws Exception {
        TaskVO taskVO = taskMap.get(id);
        Class<?> clazz = Class.forName(taskVO.getCronKey());
        FaceTask task = (FaceTask)clazz.newInstance();
        task.setTaskVO(taskVO);

        ScheduledFuture scheduledFuture = threadPoolTaskScheduler.schedule(task, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger cronTrigger = new CronTrigger(taskVO.getCronExpression());
                return cronTrigger.nextExecutionTime(triggerContext);
            }
        });

        scheduledFutureMap.put(id, scheduledFuture);
    }

    public void stop(String id) throws Exception {
        ScheduledFuture scheduledFuture = scheduledFutureMap.get(id);
        scheduledFuture.cancel(true);
    }

}
