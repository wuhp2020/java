package com.ywy.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 包含网关InclusiveGateway测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-02 11:21
 */
@SpringBootTest
public class InlusiveGatewayTest {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Test
    public void initProcessInstance() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testProcess9");
        System.out.println("流程实例ID：" + processInstance.getProcessInstanceId());
    }

    @Test
    public void completeTask() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("day", "1");
        taskService.complete("6642f817-937a-11eb-8d87-2e6e85fe7942", variables);
        System.out.println("完成任务成功！");
    }
}
