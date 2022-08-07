package com.ywy.controller;

import com.ywy.config.GlobalConfig;
import com.ywy.entity.UserInfoEntity;
import com.ywy.rest.PageParam;
import com.ywy.rest.PageResult;
import com.ywy.rest.Result;
import com.ywy.util.SecurityUtil;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 历史Controller
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-07 14:51
 */
@RestController
@RequestMapping("history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 获取用户历史任务
     * @return
     */
    @GetMapping("getInstancesByUsername")
    public Result getInstancesByUsername(@AuthenticationPrincipal UserInfoEntity userInfoEntity, PageParam pageParam) {
        try {
            HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().desc().taskAssignee(userInfoEntity.getUsername());
            long count = historicTaskInstanceQuery.count();
            List<HistoricTaskInstance> list = historicTaskInstanceQuery.listPage(pageParam.getOffset(), pageParam.getLimit());
            PageResult pageResult = new PageResult(count, list);
            return Result.success("获取用户历史任务成功", pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("获取用户历史任务失败");
        }
    }

    /**
     * 根据流程实例id获取历史任务
     * @param instanceId
     * @return
     */
    @GetMapping("getInstancesByInstanceId")
    public Result getInstancesByInstanceId(String instanceId) {
        try {
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().orderByHistoricTaskInstanceEndTime().desc()
                    .processInstanceId(instanceId).list();
            return Result.success("获取历史任务成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("获取历史任务失败");
        }
    }

    /**
     * 高亮渲染历史流程实例
     * @param instanceId
     * @param userInfoEntity
     * @return
     */
    @GetMapping("highlight")
    public Result highlight(String instanceId, @AuthenticationPrincipal UserInfoEntity userInfoEntity) {
        try {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            // 读取BPMN
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            Process process = bpmnModel.getProcesses().get(0);
            // 获取所有流程FlowElement的信息
            Collection<FlowElement> flowElements = process.getFlowElements();
            // 所有流程节点连线
            Map<String, String> map = new HashMap<>();
            for (FlowElement flowElement : flowElements) {
                // 判断是否是线条，如果是则保存连线
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    String sourceRef = sequenceFlow.getSourceRef();
                    String targetRef = sequenceFlow.getTargetRef();
                    map.put(sourceRef + targetRef, sequenceFlow.getId());
                }
            }

            // 获取所有历史流程节点
            List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).list();
            // 流程节点连线，各历史节点两两组合成key
            Set<String> set = new HashSet<>();
            for (HistoricActivityInstance i : haiList) {
                for (HistoricActivityInstance j : haiList) {
                    if (i != j) {
                        set.add(i.getActivityId() + j.getActivityId());
                    }
                }
            }

            // 高亮连线id
            Set<String> highLines = new HashSet<>();
            set.forEach(s -> highLines.add(map.get(s)));

            // 获取已经完成的节点
            List<HistoricActivityInstance> finishedList = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).finished().list();
            // 已经完成的节点高亮
            Set<String> highFinishedPoints = new HashSet<>();
            finishedList.forEach(s -> highFinishedPoints.add(s.getActivityId()));

            // 获取待办的节点
            List<HistoricActivityInstance> unfinishedList = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).unfinished().list();
            // 待办的节点高亮
            Set<String> highUnfinishedPoints = new HashSet<>();
            unfinishedList.forEach(s -> highUnfinishedPoints.add(s.getActivityId()));

            String assignName = null;
            if (GlobalConfig.isTest) {
                assignName = "zhangsan";
            } else {
                assignName = userInfoEntity.getUsername();
            }
            // 获取当前用户完成的任务
            List<HistoricTaskInstance> iFinishedList = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).taskAssignee(assignName).finished().list();
            // 当前用户完成的任务高亮
            Set<String> highIFinishedTasks = new HashSet<>();
            iFinishedList.forEach(s -> highIFinishedTasks.add(s.getTaskDefinitionKey()));

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("highLines", highLines);
            resultMap.put("highFinishedPoints", highFinishedPoints);
            resultMap.put("highUnfinishedPoints", highUnfinishedPoints);
            resultMap.put("highIFinishedTasks", highIFinishedTasks);
            return Result.success("高亮显示历史流程成功", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("高亮显示历史流程失败");
        }
    }
}
