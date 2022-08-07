package com.ywy.activiti;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 任务Task测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-01 13:42
 */
@SpringBootTest
public class TaskTest {
    @Autowired
    private TaskService taskService;

    /**
     * 查询任务
     */
    @Test
    public void getTasks() {
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list) {
            System.out.println("-------------- 任务 --------------");
            System.out.println("Id: " + task.getId());
            System.out.println("Name: " + task.getName());
            System.out.println("Assignee: " + task.getAssignee());
        }
    }

    /**
     * 查询用户的任务
     */
    @Test
    public void getTaskByAssignee() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("zhangsan").list();
        for (Task task : list) {
            System.out.println("-------------- 任务 --------------");
            System.out.println("Id: " + task.getId());
            System.out.println("Name: " + task.getName());
            System.out.println("Assignee: " + task.getAssignee());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        taskService.complete("63da2715-92b1-11eb-abe4-2e6e85fe7942");
        System.out.println("完成任务成功！");
    }

    /**
     * 拾取任务
     */
    @Test
    public void claimTask() {
        taskService.claim("b0e9732b-92b4-11eb-b05b-2e6e85fe7942", "lisi");
    }

    /**
     * 归还与交办任务
     */
    @Test
    public void setTaskAssignee() {
        // 归还候选任务
        taskService.setAssignee("b0e9732b-92b4-11eb-b05b-2e6e85fe7942", "null");
        // 交办候选任务
        taskService.setAssignee("b0e9732b-92b4-11eb-b05b-2e6e85fe7942", "zhangsan");
    }
}
