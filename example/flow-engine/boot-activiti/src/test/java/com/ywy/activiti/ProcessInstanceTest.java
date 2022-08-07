package com.ywy.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 流程实例ProcessInstance测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-01 11:03
 */
@SpringBootTest
public class ProcessInstanceTest {
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 初始化流程实例
     */
    @Test
    public void initProcessInstance() {
        // businessKey为业务表id，把业务数据和流程数据关联
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testProcess", "bKey001");
        System.out.println("流程实例ID：" + processInstance.getProcessInstanceId());
    }

    /**
     * 获取流程实例
     */
    @Test
    public void getProcessInstances() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance pi : list) {
            System.out.println("--------------流程实例---------------");
            System.out.println("processInstanceId: " + pi.getProcessInstanceId());
            System.out.println("processDefinitionId: " + pi.getProcessDefinitionId());
            System.out.println("isEnded: " + pi.isEnded());
            System.out.println("isSuspended: " + pi.isSuspended());
        }
    }

    /**
     * 挂起流程实例
     */
    @Test
    public void suspendProcessInstance() {
        runtimeService.suspendProcessInstanceById("365fa9f9-929a-11eb-a26e-2e6e85fe7942");
        System.out.println("挂起流程实例成功！");
    }

    /**
     * 激活流程实例
     */
    @Test
    public void activateProcessInstance() {
        runtimeService.activateProcessInstanceById("365fa9f9-929a-11eb-a26e-2e6e85fe7942");
        System.out.println("激活流程实例成功！");
    }

    /**
     * 删除流程实例
     */
    @Test
    public void delProcessInstance() {
        runtimeService.deleteProcessInstance("365fa9f9-929a-11eb-a26e-2e6e85fe7942", "删除测试");
        System.out.println("删除流程实例成功！");
    }
}
