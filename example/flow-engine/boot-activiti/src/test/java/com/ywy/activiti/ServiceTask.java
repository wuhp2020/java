package com.ywy.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 服务任务
 *
 * @author ywy
 * @date 2021/11/2 13:52
 */
public class ServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println(execution.getEventName());
        System.out.println(execution.getProcessDefinitionId());
        System.out.println(execution.getProcessInstanceId());

        execution.setVariable("aa", "bb");
    }
}
