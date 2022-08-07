package com.ywy.controller;

import com.ywy.config.GlobalConfig;
import com.ywy.dao.FormDataDao;
import com.ywy.entity.FormDataEntity;
import com.ywy.rest.PageResult;
import com.ywy.rest.Result;
import com.ywy.util.SecurityUtil;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务Controller
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-07 11:28
 */
@RestController
@RequestMapping("task")
public class TaskController {
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private FormDataDao formDataDao;

    /**
     * 获取我的待办任务
     * @return
     */
    @GetMapping("getTasks")
    public Result getTasks() {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }
            List<Map<String, Object>> listMap = new ArrayList<>();
            Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 100));
            List<Task> list = taskPage.getContent();
            for (Task task : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", task.getId());
                map.put("name", task.getName());
                map.put("status", task.getStatus());
                map.put("createDate", task.getCreatedDate());
                if (task.getAssignee() == null) {
                    map.put("assignee", "待拾取任务");
                } else {
                    map.put("assignee", task.getAssignee());
                }

                // 获取流程实例
                ProcessInstance processInstance = processRuntime.processInstance(task.getProcessInstanceId());
                map.put("instanceName", processInstance.getName());

                listMap.add(map);
            }
            PageResult pageResult = new PageResult(taskPage.getTotalItems(), listMap);
            return Result.success("获取我的待办任务成功", pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("获取我的待办任务失败");
        }
    }

    /**
     * 完成任务
     * @param taskId
     * @return
     */
    @PostMapping("completeTask")
    public Result completeTask(String taskId) {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }
            Task task = taskRuntime.task(taskId);
            // 如果为候选人则先拾取任务
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            return Result.success("完成任务成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("完成任务：" + taskId + "失败");
        }
    }

    /**
     * 渲染动态表单
     * @param taskId
     * @return
     */
    @PostMapping("renderForm")
    public Result renderForm(String taskId) {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }
            Task task = taskRuntime.task(taskId);
            // 表单Key必须要任务编号一模一样，因为参数需要任务key，但是无法获取，只能获取表单key
            UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId()).getFlowElement(task.getFormKey());
            if (userTask == null) {
                return Result.success("渲染动态表单成功", "无表单");
            }

            // 读取本实例下的表单数据
            List<FormDataEntity> formDataList = formDataDao.selectFormData(task.getProcessInstanceId());
            // 构建表单控件历史数据字典
            Map<String, Object> controlMap = new HashMap<>();
            for (FormDataEntity formData : formDataList) {
                controlMap.put(formData.getControlId(), formData.getControlValue());
            }

            // BPMN中存储的表单只能拿到id，所以通过id传递字段信息
            // 格式为：表单控件id-_!类型-_!名称-_!默认值-_!是否参数，例如：FormProperty_0lovri0-_!string-_!姓名-_!请输入姓名-_!f
            List<FormProperty> formProperties = userTask.getFormProperties();
            List<Map<String, Object>> listMap = new ArrayList<>();
            for (FormProperty fp : formProperties) {
                String[] arr = fp.getId().split("-_!");
                Map<String, Object> map = new HashMap<>();
                map.put("id", arr[0]);
                map.put("controlType", arr[1]);
                map.put("controlLabel", arr[2]);

                // 如果默认值是表单控件id，则获取该表单控件的值
                if (arr[3].startsWith("FormProperty_")) {
                    if (controlMap.containsKey(arr[3])) {
                        map.put("controlDefValue", controlMap.get(arr[3]));
                    } else {
                        map.put("controlDefValue", "读取失败，检查" + arr[3] + "配置");
                    }
                } else {
                    map.put("controlDefValue", arr[3]);
                }
                map.put("controlIsParam", arr[4]);

                listMap.add(map);
            }
            return Result.success("渲染动态表单成功", listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("渲染动态表单失败");
        }
    }

    /**
     * 保存动态表单数据
     * @param taskId
     * @param formData 格式：表单控件id-_!!控件值-_!是否参数!_!表单控件id-_!控件值-_!是否参数
     *                 示例：FormProperty_0lovri0-_!不是参数-_!f!_!FormProperty_1iu6onu-_!数字参数-_!s
     * @return
     */
    @PostMapping("saveFormData")
    public Result saveFormData(String taskId, String formData) {
        try {
            if (GlobalConfig.isTest) {
                securityUtil.logInAs("zhangsan");
            }
            Task task = taskRuntime.task(taskId);

            // 解析提交数据
            String[] formDataArr = formData.split("!_!");
            List<FormDataEntity> formDataList = new ArrayList<>();
            // 参数数据
            Map<String, Object> variables = new HashMap<>();
            boolean hasVariables = false;
            for (String formDataItem : formDataArr) {
                String[] arr = formDataItem.split("-_!");
                FormDataEntity formDataEntity = new FormDataEntity();
                formDataEntity.setProcDefId(task.getProcessDefinitionId());
                formDataEntity.setProcInstId(task.getProcessInstanceId());
                formDataEntity.setFormKey(task.getFormKey());
                formDataEntity.setControlId(arr[0]);
                formDataEntity.setControlValue(arr[1]);
                formDataList.add(formDataEntity);

                // 判断参数是否作为UEL表达式参数，如果是则使用控件id作为URL参数变量
                switch (arr[2]) {
                    case "f":
                        // 不是参数
                        System.out.println("控件值不作为参数");
                        break;
                    case "s":
                        // 字符类型
                        variables.put(arr[0], arr[1]);
                        hasVariables = true;
                        break;
                    case "t":
                        // 时间类型
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        variables.put(arr[0], sdf.parse(arr[1]));
                        hasVariables = true;
                        break;
                    case "b":
                        variables.put(arr[0], BooleanUtils.toBoolean(arr[1]));
                        hasVariables = true;
                        break;
                    default:
                        System.out.println("控件ID：" + arr[0] + "的参数：" + arr[2] + "不存在");
                }
            }

            // 完成任务
            if (hasVariables) {
                // 带参数完成任务
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).withVariables(variables).build());
            } else {
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).build());
            }

            // 提交表单内容写入数据库
            formDataDao.insertFormData(formDataList);
            return Result.success("保持动态表单成功", formDataList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("保持动态表单失败");
        }
    }
}
