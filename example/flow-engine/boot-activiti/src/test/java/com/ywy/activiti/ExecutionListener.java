package com.ywy.activiti;

import org.activiti.engine.delegate.DelegateExecution;

/**
 * 执行监听
 *
 * @author ywy
 * @date 2021/11/2 16:40
 */
public class ExecutionListener implements org.activiti.engine.delegate.ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        System.out.println(execution.getEventName());
        System.out.println(execution.getProcessDefinitionId());
    }
}
