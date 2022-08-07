package com.ywy.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 多实例子流程监听
 *
 * @author ywy
 * @date 2021/11/2 15:28
 */
public class MultiInstancesSubProcessListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("wukong");
    }
}
