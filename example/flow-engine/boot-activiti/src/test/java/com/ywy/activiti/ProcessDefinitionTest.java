package com.ywy.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 流程定义ProcessDefinition测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-01 9:52
 */
@SpringBootTest
public class ProcessDefinitionTest {
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询流程定义
     */
    @Test
    public void getDefinitions() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition pd : list) {
            System.out.println("------------ 流程定义 ------------");
            System.out.println("Name: " + pd.getName());
            System.out.println("Key: " + pd.getKey());
            System.out.println("ResourceName: " + pd.getResourceName());
            System.out.println("DeploymentId: " + pd.getDeploymentId());
            System.out.println("Version: " + pd.getVersion());
        }
    }

    /**
     * 删除流程定义
     */
    @Test
    public void delDefinition() {
        String pdId = "1456a750-9289-11eb-ba79-2e6e85fe7942";
        repositoryService.deleteDeployment(pdId, true);
        System.out.println("流程定义删除成功！");
    }
}
