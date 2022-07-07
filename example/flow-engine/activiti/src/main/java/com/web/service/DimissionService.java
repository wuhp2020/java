package com.web.service;

import com.google.common.collect.Lists;
import com.web.util.ActivitiUtils;
import com.web.util.ConvertUtils;
import com.web.vo.dimission.DimissionVO;
import com.web.vo.dimission.EmployeeVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DimissionService {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SpringProcessEngineConfiguration springProcessEngineConfiguration;

    public void startLeave(EmployeeVO employeeVO) {
        Map<String, Object> map = ConvertUtils.objectToMap(employeeVO);
        runtimeService.startProcessInstanceByKey("Dimission", map);
    }

    public List<DimissionVO> findByLeaderName (String leaderName) {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(leaderName).orderByTaskCreateTime().desc().list();
        List<DimissionVO> leaveVOS = Lists.newArrayList();
        tasks.stream().forEach(task -> {
            DimissionVO dimissionVO = new DimissionVO();
            String instanceId = task.getProcessInstanceId();
            ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            dimissionVO.setEmployeeVO(processInstance2EmployeeVO(instance));
            dimissionVO.setIsEnd(instance.isEnded()?"申请结束":"等待审批");
            dimissionVO.setTaskId(task.getId());
            leaveVOS.add(dimissionVO);
        });
        return leaveVOS;
    }

    public void examineLeave(String taskId) {
        taskService.complete(taskId);
    }

    public List<DimissionVO> historyLeave(String userName) {
        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().variableValueEquals("name", userName).list();
        Set<String> processInstanceIds = variableInstances.stream().map(i -> {
            return i.getProcessInstanceId();
        }).collect(Collectors.toSet());

        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery()
                .processInstanceIds(processInstanceIds).orderByProcessInstanceStartTime().desc().list();
        List<DimissionVO> leaveVOS = Lists.newArrayList();
        processInstances.stream().forEach(instance -> {
            EmployeeVO employeeVO = historicProcessInstance2EmployeeVO(instance);
            DimissionVO dimissionVO = new DimissionVO();
            dimissionVO.setEmployeeVO(employeeVO);
            dimissionVO.setIsEnd("申请结束");
            dimissionVO.setTaskId("");
            leaveVOS.add(dimissionVO);
        });

        return leaveVOS;
    }

    public EmployeeVO processInstance2EmployeeVO(ProcessInstance instance) {
        EmployeeVO employeeVO = new EmployeeVO();
        Arrays.stream(employeeVO.getClass().getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                field.set(employeeVO, runtimeService.getVariable(instance.getId(), field.getName(), field.getType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return employeeVO;
    }

    public EmployeeVO historicProcessInstance2EmployeeVO(HistoricProcessInstance instance) {
        EmployeeVO employeeVO = new EmployeeVO();
        Arrays.stream(employeeVO.getClass().getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                field.set(employeeVO, runtimeService.getVariable(instance.getId(), field.getName(), field.getType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return employeeVO;
    }

    public String activiti(String processInstanceId) throws Exception {
        return ActivitiUtils.getFlowImgByInstanceId(processInstanceId);
    }


    /**
     * 获取已经流转的线
     *
     * @param bpmnModel
     * @param historicActivityInstances
     * @return
     */
    private static List<String> getHighLightedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        // 高亮流程已发生流转的线id集合
        List<String> highLightedFlowIds = new ArrayList<>();
        // 全部活动节点
        List<FlowNode> historicActivityNodes = new ArrayList<>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstances = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            historicActivityNodes.add(flowNode);
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstances.add(historicActivityInstance);
            }
        }

        FlowNode currentFlowNode = null;
        FlowNode targetFlowNode = null;
        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstances) {
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlows = currentFlowNode.getOutgoingFlows();

            /**
             * 遍历outgoingFlows并找到已已流转的 满足如下条件认为已已流转： 1.当前节点是并行网关或兼容网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最早的流转节点视为有效流转
             */
            if ("parallelGateway".equals(currentActivityInstance.getActivityType()) || "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配流程目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicActivityNodes.contains(targetFlowNode)) {
                        highLightedFlowIds.add(targetFlowNode.getId());
                    }
                }
            } else {
                List<Map<String, Object>> tempMapList = new ArrayList<>();
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
                        if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("highLightedFlowId", sequenceFlow.getId());
                            map.put("highLightedFlowStartTime", historicActivityInstance.getStartTime().getTime());
                            tempMapList.add(map);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(tempMapList)) {
                    // 遍历匹配的集合，取得开始时间最早的一个
                    long earliestStamp = 0L;
                    String highLightedFlowId = null;
                    for (Map<String, Object> map : tempMapList) {
                        long highLightedFlowStartTime = Long.valueOf(map.get("highLightedFlowStartTime").toString());
                        if (earliestStamp == 0 || earliestStamp >= highLightedFlowStartTime) {
                            highLightedFlowId = map.get("highLightedFlowId").toString();
                            earliestStamp = highLightedFlowStartTime;
                        }
                    }

                    highLightedFlowIds.add(highLightedFlowId);
                }

            }

        }
        return highLightedFlowIds;
    }
}
