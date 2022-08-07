package com.ywy.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 历史记录HistoricTaskInstance测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-01 14:50
 */
@SpringBootTest
public class HistoricTaskInstanceTest {
    @Autowired
    private HistoryService historyService;

    /**
     * 根据用户查询历史记录
     */
    @Test
    public void getHistoricTaskInstanceByUser() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("zhangsan")
                .orderByHistoricTaskInstanceEndTime().asc()
                .list();
        for (HistoricTaskInstance hti : list) {
            System.out.println("------------- 历史记录 ---------------");
            System.out.println("Id: " + hti.getId());
            System.out.println("ProcessInstanceId: " + hti.getProcessInstanceId());
            System.out.println("Name: " + hti.getName());
        }
    }

    /**
     * 根据流程实例id查询历史记录
     */
    @Test
    public void getHistoricInstanceByPid() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("63ce8e51-92b1-11eb-abe4-2e6e85fe7942")
                .orderByHistoricTaskInstanceEndTime().asc()
                .list();
        for (HistoricTaskInstance hti : list) {
            System.out.println("------------- 历史记录 ---------------");
            System.out.println("Id: " + hti.getId());
            System.out.println("ProcessInstanceId: " + hti.getProcessInstanceId());
            System.out.println("Name: " + hti.getName());
        }
    }
}
