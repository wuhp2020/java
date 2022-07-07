package com.web.service;

import com.web.vo.task.StartTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private Scheduler scheduler;

    /**
     *
     * @param startTaskVO
     */
    public void start(StartTaskVO startTaskVO) {
        try {
            Class<? extends QuartzJobBean> jobClass = (Class<? extends QuartzJobBean>) Class.forName(startTaskVO.getJobClass());
            String jobName = startTaskVO.getJobName();
            String jobGroupName = startTaskVO.getJobGroupName();
            String jobTime = startTaskVO.getJobTime();
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            // 任务名称和组构成任务key
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 定义调度触发规则
            // 使用cornTrigger规则
            // 触发器key
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).startNow().build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
