package com.ywy.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程部署Deployment测试
 * @author ywy
 * @version 1.0.0
 * @date 2021-03-31 15:25
 */
@SpringBootTest
public class DeploymentTest {
    @Autowired
    private RepositoryService repositoryService;

    /**
     * bpmn文件方式部署流程
     */
    @Test
    public void initDeploymentBPMN() {
        String fileName = "bpmn/processRuntime.bpmn";
        String pngName = "bpmn/deployment.png";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(fileName)
//                .addClasspathResource(pngName)
                .name("流程部署测试processRuntime")
                .deploy();
        System.out.println(deployment.getName());
    }

    /**
     * zip文件方式部署流程
     */
    @Test
    public void initDeploymentZip() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bpmn/deploymentV2.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("流程部署测试zip")
                .deploy();
        System.out.println(deployment.getName());
    }

    /**
     * 查询流程部署
     */
    @Test
    public void getDeployments() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for (Deployment dep : list) {
            System.out.println("-------------- 流程部署 --------------");
            System.out.println("Id: " + dep.getId());
            System.out.println("Name: " + dep.getName());
            System.out.println("DeploymentTime: " + dep.getDeploymentTime());
            System.out.println("Key: " + dep.getKey());
        }
    }
}
