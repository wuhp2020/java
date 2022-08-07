package com.ywy.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 多实例任务开始监听
 *
 * @author ywy
 * @date 2021/11/2 15:28
 */
public class MultiInstancesStartListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        List<String> assigneeList = new ArrayList<>();
        assigneeList.add("baijie");
        assigneeList.add("wukong");
        assigneeList.add("admin");
        execution.setVariable("assigneeList", assigneeList);
    }
}
