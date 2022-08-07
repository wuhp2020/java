package com.ywy.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 并行网关ParallelGateway测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-02 11:21
 */
@SpringBootTest
public class ParallelGatewayTest {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Test
    public void initProcessInstance() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testProcess7");
        System.out.println("流程实例ID：" + processInstance.getProcessInstanceId());
    }

    @Test
    public void completeTask() {
        taskService.complete("de99b46a-936f-11eb-b7bf-2e6e85fe7942");
        System.out.println("完成任务成功！");
    }
}
