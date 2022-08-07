package com.ywy.activiti;

import com.ywy.util.SecurityUtil;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * ProcessRuntime测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-02 14:38
 */
@SpringBootTest
public class ProcessRuntimeTest {
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 获取流程实例
     */
    @Test
    public void getProcessInstance() {
        securityUtil.logInAs("zhangsan");
        Page<ProcessInstance> processInstancePage = processRuntime.processInstances(Pageable.of(0, 100));
        System.out.println("流程实例数量：" + processInstancePage.getTotalItems());
        List<ProcessInstance> list = processInstancePage.getContent();
        for (ProcessInstance pi : list) {
            System.out.println("---------------- 流程实例 -------------------");
            System.out.println("Id: " + pi.getId());
            System.out.println("Name: " + pi.getName());
            System.out.println("StartDate: " + pi.getStartDate());
            System.out.println("Status: " + pi.getStatus());
            System.out.println("ProcessDefinitionId: " + pi.getProcessDefinitionId());
            System.out.println("ProcessDefinitionKey: " + pi.getProcessDefinitionKey());
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        securityUtil.logInAs("zhangsan");
        processRuntime.start(ProcessPayloadBuilder.start().withProcessDefinitionKey("testProcess10").withName("流程实例名称").withBusinessKey("自定义key").build());
    }

    /**
     * 删除流程实例
     */
    @Test
    public void delProcessInstance() {
        securityUtil.logInAs("zhangsan");
        processRuntime.delete(ProcessPayloadBuilder.delete().withProcessInstanceId("00cd7b6e-9382-11eb-88b5-2e6e85fe7942").build());
    }

    /**
     * 挂起流程实例
     */
    @Test
    public void suspendProcessInstance() {
        securityUtil.logInAs("zhangsan");
        processRuntime.suspend(ProcessPayloadBuilder.suspend().withProcessInstanceId("00cd7b6e-9382-11eb-88b5-2e6e85fe7942").build());
    }

    /**
     * 激活流程实例
     */
    @Test
    public void resumeProcessInstance() {
        securityUtil.logInAs("zhangsan");
        processRuntime.resume(ProcessPayloadBuilder.resume().withProcessInstanceId("00cd7b6e-9382-11eb-88b5-2e6e85fe7942").build());
    }

    /**
     * 流程实例参数
     */
    @Test
    public void getVariables() {
        securityUtil.logInAs("zhangsan");
        List<VariableInstance> list = processRuntime.variables(ProcessPayloadBuilder.variables().withProcessInstanceId("0a630bea-9372-11eb-9e8e-2e6e85fe7942").build());
        for (VariableInstance vi : list) {
            System.out.println("--------------- 流程实例参数 ---------------");
            System.out.println("Name: " + vi.getName());
            System.out.println("Value: " + vi.getValue());
            System.out.println("TaskId: " + vi.getTaskId());
            System.out.println("ProcessInstanceId: " + vi.getProcessInstanceId());
        }
    }
}
