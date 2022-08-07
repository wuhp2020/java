package com.ywy.activiti;

import org.activiti.engine.delegate.DelegateTask;

/**
 * 任务监听
 *
 * @author ywy
 * @date 2021/11/2 16:30
 */
public class TaskListener implements org.activiti.engine.delegate.TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println(delegateTask.getAssignee());
    }
}
