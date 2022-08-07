package com.ywy.activiti;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 错误服务任务
 *
 * @author ywy
 * @date 2021/11/2 13:52
 */
public class ErrorServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        throw new BpmnError("Activity_0l0y0u4");
    }
}
