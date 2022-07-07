package com.spring.config;

import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class ActivitiConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(){
        SpringProcessEngineConfiguration spec = new SpringProcessEngineConfiguration();
        spec.setActivityFontName("宋体");
        spec.setAnnotationFontName("宋体");
        spec.setLabelFontName("宋体");
        spec.setDataSource(dataSource);
        spec.setTransactionManager(platformTransactionManager);
        spec.setDatabaseSchemaUpdate("true");
        Resource[] resources = null;
        // 启动自动部署流程
        try {
            resources = new PathMatchingResourcePatternResolver().getResources("classpath*:bpmn/*.bpmn");
        } catch (IOException e) {
            e.printStackTrace();
        }
        spec.setDeploymentResources(resources);
        return spec;
    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean(){
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(springProcessEngineConfiguration());
        return processEngineFactoryBean;
    }

    // 业务流程的定义相关服务
    @Bean
    public RepositoryService repositoryService() throws Exception{
        return processEngineFactoryBean().getObject().getRepositoryService();
    }

    // 流程对象实例相关服务
    @Bean
    public RuntimeService runtimeService() throws Exception{
        return processEngineFactoryBean().getObject().getRuntimeService();
    }

    // 流程任务节点相关服务
    @Bean
    public TaskService taskService() throws Exception{
        return processEngineFactoryBean().getObject().getTaskService();
    }

    // 流程历史信息相关服务
    @Bean
    public HistoryService historyService() throws Exception{
        return processEngineFactoryBean().getObject().getHistoryService();
    }

    // 表单引擎相关服务
    @Bean
    public FormService formService() throws Exception{
        return processEngineFactoryBean().getObject().getFormService();
    }

    // 用户以及组管理相关服务
    @Bean
    public IdentityService identityService() throws Exception{
        return processEngineFactoryBean().getObject().getIdentityService();
    }

    // 管理和维护相关服务
    @Bean
    public ManagementService managementService() throws Exception{
        return processEngineFactoryBean().getObject().getManagementService();
    }

    // 动态流程服务
    @Bean
    public DynamicBpmnService dynamicBpmnService() throws Exception{
        return processEngineFactoryBean().getObject().getDynamicBpmnService();
    }
}
