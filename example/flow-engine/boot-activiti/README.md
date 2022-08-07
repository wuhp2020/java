学习自慕课网《Activiti7精讲&Java通用型工作流开发实战》视频。

# 1、Activiti介绍
Activiti项目是基于Apache License许可的开源项目。前身是JBPMN，作者Tom Baeyens，2010年立项。Activiti项目支持BPMN标准。

## 1.1、Activiti7新特性说明
与SpringBoot更好的原生支持。  
引入SpringSecurity作为默认用户与角色的默认安全机制。  
核心API进行了封装。  
对云发布，分布式支持等。

接口变化：
   ![接口变化](doc/images/1.png)

## 1.2、工作流常见业务场景
1. 线性审批
   ![线性审批](doc/images/2.png)
   
2. 会签审批
   ![会签审批](doc/images/3.png)
   
3. 条件流程
   ![条件流程](doc/images/4.png)
   
## 1.3、BPMN2.0简介
1. BPMN2.0标准概述  
BPMN：Business Process Model and Notation，业务流程建模标注。  
BPMN的出现弥补了从业务流程设计到流程开发的间隙。  
BPMN是OMG国际标准组织于2011年推出的BPMN2.0标准。  
BPMN2.0支持强大的可视化设计、执行和维护流程，BPMN2.0标准的制定统一了业务流程描述文件与业务流程符号，使不同企业能够在相同的标准下进行交互开发，极大的推进了行业进步。
BPMN2.0包括对绘制业务流程图标的定义，说明流程的含义；对业务流程描述的定义（XML）。

2. 常用工具解释  
StartEvent：整个流程的开始节点，有且只有一个  
EndEvent：整个流程的结束节点  
UserTask：用户任务，一个流程可以有多个任务  
ParamlleGateway：并行网关，网关包括并行网关、排它网关、包含网关、事件网关  
SubProcess：子流程，用于流程过于复杂进行分支或提炼相同流程  
Pool：甬道，可以将不同的角色放到不同的甬道里，明确每个角色所在的位置以及流程的流转内容  
BoundaryEvent：边界事件

3. Activiti7对BPMN的自动转化
   ![转化](doc/images/5.png)
   
## 1.4 Activiti7经典类
   ![经典类](doc/images/6.png)

1. 流程部署Deployment类
   ![流程部署Deplayment](doc/images/7.png)

2. 流程定义ProcessDefinition类 
   Deployment：添加资源文件、获取部署信息、部署时间  
   ProcessDefinition：获取版本号、key、资源名称、部署ID等
   
3. 流程实例ProcessInstance
   ProcessDefinition与ProcessInstance是一对多关系
   
## 1.5、任务的类型
任务的图形化是以矩形为基础，在左侧添加具体的图标，用来描述一种特定任务类型。用户任务需要人来参与，需要人为触发。

用户任务的属性：  
1. Assignee：执行人/代理人
2. Candidate Users：候选人
3. Candidate Groups：候选组
4. Due Date：任务到期时间

## 1.6、查询历史记录
历史综合信息：HistoricTaskInstance  
历史变量：HistoricVariableInstance

## 1.7、UEL表达式
EL：Expression Language，表达式语言  
UEL：Unified Expression Language，统一表达式语言

### 表达式描述 
   以“${”开始，以“}”结束，例如：${day>100}  
   支持逻辑运算符，例如${userName == "zhangsan" and pwd == "123"}  
   支持变量与实体类赋值

### 对应Activiti数据表
1. act_ru_variable运行时参数表
2. act_hi_varinst历史参数表

### UEL表达式的保留字
   ![UEL表达式的保留字](doc/images/8.png)

### UEL表达式的运算符
   ![UEL表达式的运算符](doc/images/9.png)

## 1.8、BPMN2.0网关
并行网关、排它网关、包容网关、事件网关

## 1.9、高级知识
### 1.9.1、监听器
监听器：执行监听器、任务监听器

执行监听器的作用：存储读取变量、处理业务信息  
执行监听器属性：
   ![执行监听器属性](doc/images/10.png)

### 1.9.2、事件
事件分类：开始与结束事件、中间事件、边界事件
   ![事件分类](doc/images/11.png)

（1）定时事件  
使用场景：  
①指定日期开启流程实例  
②24小时任务未办理短信提醒  
③3天未审核则主管介入  
定时事件类型：  
①Time date：日期，什么时间触发  
例子：2021-08-30T15:23:59  
年月日和时间之间用T分隔  
②Time duration：持续，延时多长时间后触发  
例子：P1DT1M-一天一分钟执行一次  
P：开始标记；Y：年；M：月；W：周；D：天；T：时间和日期分割标记；H：小时；M：分钟；S：秒  
T不能省略  
③Time cycle：循环，循环规则  
例子：循环3次/开始循环时间/每次间隔：R3/2021-07-30T19:12:00/PT1M  
无限循环/时间间隔/结束时间：R/PT1M/2021-01-01  
时间可以使用变量${}

（2）信号事件  
信号事件：Catching捕获事件、Throwing抛出事件  
特性：广播，一对多  
例如用于启动预案  
代码：
```java
runtimeService.signalEventReceived("Signal_1auq05c");
```

（3）消息事件  
典型应用：跨实例进行触发，跨任务进行触发。  
常用于撤回任务。

（4）错误事件  
边界事件、结束事件、主流程开始事件。

（5）补偿事件  
特点：  
①任务到达时激活  
②流程实例结束时挂起（其他事件任务结束时挂起）  
③子流程需要任务到达补偿节点才能激活，而非子流程开始就激活

（6）取消事件  
事务子流程中用取消事件回退已经执行过的操作。

（7）BPMN-JS额外事件  
escalationEventDefinition升级事件  
linkEventDefinition连接事件  
conditionalEventDefinition条件事件  
activiti不支持。

### 1.9.3、任务
任务分类：
   ![任务分类](doc/images/12.png)

（1）手工任务  
不会做任何操作，是用来留痕迹的。

（2）脚本任务scriptTask  
可以用监听、服务任务和事件代替。

（3）业务规则任务businessRuleTask
与脚本任务类似，在不重新发布任务的情况下改变业务逻辑。  
需要使用规则引擎，学习新的语法。

（4）接收任务receiveTask  
触发语句`runtimeService.trigger(execution.getId());`

（5）发送任务sendTask  
与接收任务并不对应，不会触发。

（6）服务任务serviceTask
使用场景：发短信、发邮件、调用其他接口  
注意事项：自动执行、需要执行类、执行类必须继承JavaDelegate接口

### 1.9.4、子流程
（1）嵌入子流程：主流程与子流程在同一个BPMN中，主要用来区分功能块，一般作为局部通用的处理逻辑。  
（2）调用子流程：主流程与被调用的主流程在不同的BPMN中，主要用于供多个主流程复用。

### 1.9.5、多实例任务
用于会签（并行）、多子流程业务（并行）、动态顺序审批（串行）  
多实例任务完成条件：  
①实例总数：nrOfInstances；  
②当前还没有完成的实例个数：nrOfActiveInstances；  
③已经完成的实例个数：nrOfCompletedInstances。

## 1.10、Activiti7新特性使用
### 1.10.1、ProcessRuntime
```java
public interface ProcessRuntime {
  ProcessRuntimeConfiguration configuration();
  ProcessDefinition processDefinition(String processDefinitionId);
  Page processDefinitions(Pageable pageable);
  Page processDefinitions(Pageable pageable, GetProcessDefinitionsPayload payload);
  ProcessInstance start(StartProcessPayload payload);
  Page processInstances(Pageable pageable);
  Page processInstances(Pageable pageable, GetProcessInstancesPayload payload);
  ProcessInstance processInstance(String processInstanceId);
  ProcessInstance suspend(SuspendProcessPayload payload);
  ProcessInstance resume(ResumeProcessPayload payload);
  ProcessInstance delete(DeleteProcessPayload payload);
  void signal(SignalPayload payload);
  ...
}
```
为了与ProcessRuntime API交互，当前登录的用户必须具有“ACTIVITI_USER”角色。

### 1.10.2、TaskRuntime
```java
public interface TaskRuntime {
  TaskRuntimeConfiguration configuration();
  Task task(String taskId);
  Page tasks(Pageable pageable);
  Page tasks(Pageable pageable, GetTasksPayload payload);
  Task create(CreateTaskPayload payload);
  Task claim(ClaimTaskPayload payload);
  Task release(ReleaseTaskPayload payload);
  Task complete(CompleteTaskPayload payload);
  Task update(UpdateTaskPayload payload);
  Task delete(DeleteTaskPayload payload);
  ...
}
```
为了以用户身份与TaskRuntime API交互，需要具有角色“ACTIVITI_USER”。

## 1.11、注意事项
（1）taskService.complete()不能覆盖变量，TaskListener和taskRuntime.complete可以覆盖。  
（2）Activiti取消了表单和用户组功能。  
（3）前端调试使用debugger和F12  
（4）postman等工具调试失败检查url是否拼错、参数是否拼错、参数数量是否一致

# 2、SpringSecurity介绍
SpringSecurity主要功能：认证、授权

用户的三种来源：
1. application.properties配置用户
2. 代码中配置内存用户
3. 从数据库中加载用户

# 3、动态表单字段
Activiti7使用bpmnjs的表单时，因为在代码中获取不到除编号和类型外的其他内容，所以在此采用拼接字符串的方式描述表单信息。

## 3.1、表单字段约定
表单控件命名约定：表单控件id-_!类型-_!名称-_!默认值-_!是否参数  
表单控件id：即bpmnjs的表单字段编号  
类型：不能为int，可以为string、long、cUser  
默认值：无、字符常量、表单控件id（用于获取已设置的值，以FormProperty_开头）  
是否参数（是否作为UEL表达式参数）：f-不是参数、s-字符串、t-时间、b-布尔

注意：表单key必须与任务Key一致，获取表单参数需要任务key但无法获取，所以使用表单key代替任务key。  
表单控件字段作为UEL表达式参数时，使用表单控件id作为UEL参数变量，便于UEL表达式和控件id绑定。

## 3.2、表单提交参数约定
动态表单提交内容约定：表单控件id-_!控件值-_!是否参数!_!表单控件id-_!控件值-_!是否参数

# 4、bpmnjs
bpmnjs是基于webpack的，需要使用npm安装和运行。  
安装：npm install  
运行：npm run dev

项目中bpmnjs放在resource/static/bpmnjs下，如果需要修改bpmnjs内容则要重新运行编译。
编译好的文件在public目录下，正式项目中不需要使用npm，直接启动项目即可。


# 5、部署
## 5.1、打包jar改为war
如果想部署为war包按以下步骤设置：  
（1）修改启动类
```java
@SpringBootApplication(scanBasePackages = {"com.ywy"})
public class BootActivitiApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BootActivitiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BootActivitiApplication.class, args);
    }
}
```

（2）修改pom.xml
pom.xml添加`<packaging>war</packaging>`  
不要包含tomcat依赖。

## 5.2、Linux部署
修改配置：  
（1）关闭测试`GlobalConfig.isTest = false`  
（2）修改上传路径：`GlobalConfig.bpmnFileUploadPath`为对应操作系统的路径    
（3）服务安装JDK和数据库  
（4）启动项目后会自动创建activiti相关表，然后执行数据库脚本`doc/db/repair-activiti-missing-fields.sql`修复activiti缺失字段和创建相关表
### 5.2.1 jar包部署
（1）项目打jar包：`mvn clean package`，文件重名命为boot-activiti.jar  
（2）将jar包上传至Linux服务器  
（3）运行项目：`nohup java -jar boot-activiti.jar >/dev/null &`

### 5.2.2 war包部署
（1）下载tomcat8  
下载命令：`wget https://dlcdn.apache.org/tomcat/tomcat-8/v8.5.72/bin/apache-tomcat-8.5.72.tar.gz`  
解压tocmat：`tar -zxvf apache-tomcat-8.5.72.tar.gz`  
（2）按照5.1步骤配置成war包模式  
（3）项目打war包：`mvn clean package`，文件重名命为boot-activiti.war  
（4）将war包上传至tomcat的webapps目录
（5）进入tomcat的bin目录，执行`sudo sh startup.sh`启动tomcat，此时访问项目需要带文件名boot-activiti

## 5.3、Windows部署
运行前配置与Linux部署一样。
### 5.3.1 jar包部署 
（1）项目打jar包：`mvn clean package`，文件重名命为boot-activiti.jar  
（2）运行项目需要在cmd执行`java -jar boot-activiti.jar`

### 5.3.2 war包部署
（1）下载tomcat8  
下载链接：`https://dlcdn.apache.org/tomcat/tomcat-8/v8.5.72/bin/apache-tomcat-8.5.72-windows-x64.zip`  
下载后进行解压。  
（2）按照5.1步骤配置成war包模式  
（3）项目打war包：`mvn clean package`，文件重名命为boot-activiti.war  
（4）将war包放到tomcat的webapps目录
（5）进入tomcat的bin目录，双击startup.bat启动tomcat，此时访问项目需要带文件名boot-activiti

## 6、运行效果
（1）登录
   ![登录](doc/images/run01.png)
（2）主页
   ![主页](doc/images/run02.png)
（3）流程部署
   ![流程部署](doc/images/run03.png)
（4）在线绘制流程
   ![在线绘制流程](doc/images/run04.png)
（5）流程实例
   ![流程实例](doc/images/run05.png)
（6）历史流程
   ![历史流程](doc/images/run06.png)
（7）待办任务
   ![待办任务](doc/images/run07.png)
（8）任务动态表单
   ![任务动态表单](doc/images/run08.png)
（9）历史任务
   ![历史任务](doc/images/run09.png)