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
            dimissionVO.setIsEnd(instance.isEnded()?"????????????":"????????????");
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
            dimissionVO.setIsEnd("????????????");
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
     * ????????????????????????
     *
     * @param bpmnModel
     * @param historicActivityInstances
     * @return
     */
    private static List<String> getHighLightedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        // ?????????????????????????????????id??????
        List<String> highLightedFlowIds = new ArrayList<>();
        // ??????????????????
        List<FlowNode> historicActivityNodes = new ArrayList<>();
        // ??????????????????????????????
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
        // ???????????????????????????????????????????????????outgoingFlows?????????????????????
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstances) {
            // ??????????????????????????????????????????outgoingFlows??????
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlows = currentFlowNode.getOutgoingFlows();

            /**
             * ??????outgoingFlows???????????????????????? ??????????????????????????????????????? 1.??????????????????????????????????????????????????????outgoingFlows???????????????????????????????????????????????????????????? 2.???????????????????????????????????????????????????outgoingFlows?????????????????????????????????????????????????????????
             */
            if ("parallelGateway".equals(currentActivityInstance.getActivityType()) || "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                // ????????????????????????????????????????????????????????????
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
                    // ?????????????????????????????????????????????????????????
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
