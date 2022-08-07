package com.ywy.controller;

import com.ywy.config.GlobalConfig;
import com.ywy.entity.UserInfoEntity;
import com.ywy.rest.PageParam;
import com.ywy.rest.PageResult;
import com.ywy.rest.Result;
import com.ywy.util.SecurityUtil;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程实例Controller
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-07 9:46
 */
@RestController
@RequestMapping("processInstance")
public class ProcessInstanceController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 获取流程实例列表
     * @return
     */
    @GetMapping("getInstances")
    public Result getInstances(PageParam pageParam) {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }

            List<Map<String, Object>> listMap = new ArrayList<>();
            Page<ProcessInstance> page = processRuntime.processInstances(Pageable.of(pageParam.getOffset(), pageParam.getLimit()));
            List<ProcessInstance> list = page.getContent();
            // 根据开始时间排序
            list.sort((o1, o2) -> o2.getStartDate().compareTo(o1.getStartDate()));
            for (ProcessInstance pi : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", pi.getId());
                map.put("name", pi.getName());
                map.put("startDate", pi.getStartDate());
                map.put("processDefinitionId", pi.getProcessDefinitionId());
                map.put("processDefinitionKey", pi.getProcessDefinitionKey());
                map.put("processDefinitionVersion", pi.getProcessDefinitionVersion());
                map.put("status", pi.getStatus());

                // 获取流程定义
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId()).singleResult();
                map.put("deploymentId", processDefinition.getDeploymentId());
                map.put("resourceName", processDefinition.getResourceName());

                listMap.add(map);
            }
            PageResult pageResult = new PageResult(page.getTotalItems(), listMap);
            return Result.success("获取流程实例成功", pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("获取流程实例失败");
        }
    }

    /**
     * 启动流程实例
     * @param processDefinitionKey
     * @param instanceName
     * @return
     */
    @PostMapping("startInstance")
    public Result startInstance(String processDefinitionKey, String instanceName) {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }
            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder.start().withProcessDefinitionKey(processDefinitionKey).withName(instanceName).withBusinessKey("自定义BusinessKey").build());
            return Result.success("启动流程实例成功", processInstance.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("启动流程实例失败");
        }
    }

    /**
     * 挂起流程实例
     * @param instanceId
     * @return
     */
    @PostMapping("suspendInstance")
    public Result suspendInstance(String instanceId) {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }
            ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder.suspend().withProcessInstanceId(instanceId).build());
            return Result.success("挂起流程实例成功", processInstance.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("挂起流程实例失败");
        }
    }

    /**
     * 激活流程实例
     * @param instanceId
     * @return
     */
    @PostMapping("resumeInstance")
    public Result resumeInstance(String instanceId) {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }
            ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder.resume().withProcessInstanceId(instanceId).build());
            return Result.success("激活流程实例成功", processInstance.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("激活流程实例失败");
        }
    }

    /**
     * 删除流程实例
     * @param instanceId
     * @return
     */
    @PostMapping("deleteInstance")
    public Result deleteInstance(String instanceId) {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }
            ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder.delete().withProcessInstanceId(instanceId).build());
            return Result.success("删除流程实例成功", processInstance.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("删除流程实例失败");
        }
    }

    /**
     * 获取流程实例参数
     * @param instanceId
     * @return
     */
    @GetMapping("variables")
    public Result variables(String instanceId) {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }
            List<VariableInstance> variables = processRuntime.variables(ProcessPayloadBuilder.variables().withProcessInstanceId(instanceId).build());
            return Result.success("查询流程实例参数成功", variables);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("查询流程实例参数失败");
        }
    }
}
