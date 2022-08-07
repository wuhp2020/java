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
 * UEL测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-02 9:56
 */
@SpringBootTest
public class UELTest {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    /**
     * 启动流程实例带参数
     */
    @Test
    public void initProcessInstanceWithArgs() {
        // 流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", "zhangsan");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testProcess4", "bKey002", variables);
        System.out.println("流程实例ID：" + processInstance.getProcessInstanceId());
    }

    /**
     * 完成任务带参数
     */
    @Test
    public void completeTaskWithArgs() {
        // 流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("pay", "101");
        taskService.complete("2ebafccf-935d-11eb-90cf-2e6e85fe7942", variables);
        System.out.println("完成任务成功！");
    }

    /**
     * 启动流程实例带参数，使用实体类
     */
    @Test
    public void initProcessInstanceWithClassArgs() {
        UELPojo uelPojo = new UELPojo();
        uelPojo.setUser("zhangsan");
        // 流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("uelpojo", uelPojo);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testProcess6", "bKey002", variables);
        System.out.println("流程实例ID：" + processInstance.getProcessInstanceId());
    }

    /**
     * 完成任务带参数，指定多个候选人
     */
    @Test
    public void completeTaskWithCandiDateArgs() {
        // 流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("cusers", "lisi,wangwu");
        taskService.complete("8d221835-935f-11eb-bfdc-2e6e85fe7942", variables);
        System.out.println("完成任务成功！");
    }

    /**
     * 直接指定流程变量
     */
    @Test
    public void otherArgs() {
        runtimeService.setVariable("8cfa92ff-935f-11eb-bfdc-2e6e85fe7942", "pay", "101");
    }

    /**
     * 局部变量
     */
    @Test
    public void otherLocalArgs() {
        runtimeService.setVariableLocal("8cfa92ff-935f-11eb-bfdc-2e6e85fe7942", "pay", "101");
    }
}
