package com.ywy.activiti;

import com.ywy.util.SecurityUtil;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TaskRuntime测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-02 15:24
 */
@SpringBootTest
public class TaskRuntimeTest {
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 获取当前登录用户任务
     */
    @Test
    public void getTasks() {
        securityUtil.logInAs("zhangsan");
        Page<Task> tasksPage = taskRuntime.tasks(Pageable.of(0, 100));
        List<Task> list = tasksPage.getContent();
        for (Task task : list) {
            System.out.println("---------------- 用户任务 ----------------");
            System.out.println("Id: " + task.getId());
            System.out.println("Name: " + task.getName());
            System.out.println("Status: " + task.getStatus());
            System.out.println("CreatedDate: " + task.getCreatedDate());
            if (task.getAssignee() == null) {
                System.out.println("Assignee: 待拾取任务");
            } else {
                System.out.println("Assignee: " + task.getAssignee());
            }
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        securityUtil.logInAs("zhangsan");
        Task task = taskRuntime.task("b0e9732b-92b4-11eb-b05b-2e6e85fe7942");
        // 如果为候选人则拾取任务
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
        System.out.println("任务执行完成！");
    }
}
