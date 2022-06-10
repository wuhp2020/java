
高并发下接口幂等性解决方案:
1. 查询操作: 查询一次和查询多次, 在数据不变的情况下, 查询结果是一样的. select是天然的幂等操作

2. 删除操作: 删除操作也是幂等的, 删除一次和多次删除都是把数据删除.
(注意可能返回结果不一样, 删除的数据不存在, 返回0, 删除的数据多条, 返回结果多个)

3. 唯一索引: 防止新增脏数据. 比如: 支付宝的资金账户, 支付宝也有用户账户,
每个用户只能有一个资金账户, 怎么防止给用户创建资金账户多个, 那么给资金账户表中的用户ID加唯一索引,
所以一个用户新增成功一个资金账户记录. 要点: 唯一索引或唯一组合索引来防止新增数据存在脏数据
（当表存在唯一索引, 并发时新增报错时, 再查询一次就可以了, 数据应该已经存在了, 返回结果即可）

4. token机制: 防止页面重复提交. 业务要求: 页面的数据只能被点击提交一次,
发生原因: 由于重复点击或者网络重发, 或者nginx重发等情况会导致数据被重复提交,
解决办法: 集群环境采用token加redis(redis单线程的，处理需要排队),
单JVM环境: 采用token加redis或token加jvm内存.
处理流程: 1. 数据提交前要向服务的申请token, token放到redis或jvm内存,
token有效时间. 2. 提交后后台校验token, 同时删除token, 生成新的token返回.
token特点: 要申请, 一次有效性, 可以限流. 注意: redis要用删除操作来判断token,
删除成功代表token校验通过, 如果用select+delete来校验token, 存在并发问题, 不建议使用.

5. 悲观锁: 获取数据的时候加锁获取.
select * from table_xxx where id='xxx' for update; 注意: id字段一定是主键或者唯一索引,
不然是锁表, 会死人的悲观锁使用时一般伴随事务一起使用, 数据锁定时间可能会很长, 根据实际情况选用.

6. 乐观锁: 乐观锁只是在更新数据那一刻锁表, 其他时间不锁表, 所以相对于悲观锁, 效率更高.
乐观锁的实现方式多种多样可以通过version或者其他状态条件1.
通过版本号实现update table_xxx set name=#name#,version=version+1 where version=#version#
2. 通过条件限制 update table_xxx set avai_amount=avai_amount-#subAmount#
where avai_amount-#subAmount# >= 0
要求: quality-#subQuality# >= , 这个情景适合不用版本号, 只更新是做数据安全校验,
适合库存模型, 扣份额和回滚份额, 性能更高.

7. 分布式锁: 还是拿插入数据的例子, 如果是分布是系统, 构建全局唯一索引比较困难,
例如唯一性的字段没法确定, 这时候可以引入分布式锁, 通过第三方的系统(redis或zookeeper),
在业务系统插入数据或者更新数据, 获取分布式锁, 然后做操作, 之后释放锁, 这样其实是把多线程并发的锁的思路,
引入多个系统, 也就是分布式系统中得解决思路. 要点: 某个长流程处理过程要求不能并发执行,
可以在流程执行之前根据某个标志(用户ID+后缀等)获取分布式锁, 其他流程执行时获取锁就会失败,
也就是同一时间该流程只能有一个能执行成功, 执行完成后, 释放分布式锁(分布式锁要第三方系统提供).

8. select + insert: 并发不高的后台系统, 或者一些任务JOB, 为了支持幂等, 支持重复执行,
简单的处理方法是, 先查询下一些关键数据, 判断是否已经执行过, 在进行业务处理, 就可以了.
注意: 核心高并发流程不要用这种方法.

9. 状态机幂等: 在设计单据相关的业务, 或者是任务相关的业务, 肯定会涉及到状态机(状态变更图),
就是业务单据上面有个状态, 状态在不同的情况下会发生变更, 一般情况下存在有限状态机, 这时候,
如果状态机已经处于下一个状态, 这时候来了一个上一个状态的变更, 理论上是不能够变更的, 这样的话,
保证了有限状态机的幂等. 注意: 订单等单据类业务, 存在很长的状态流转, 一定要深刻理解状态机,
对业务系统设计能力提高有很大帮助.

10. 对外提供接口的api如何保证幂等: 如银联提供的付款接口, 需要接入商户提交付款请求时附带:
source来源, seq序列号, source+seq在数据库里面做唯一索引,
防止多次付款(并发时, 只能处理一个请求) .重点: 对外提供接口为了支持幂等调用,
接口有两个字段必须传, 一个是来源source, 一个是来源方序列号seq, 这个两个字段在提供方系统里面做联合唯一索引,
这样当第三方调用时, 先在本方系统里面查询一下, 是否已经处理过, 返回相应处理结果;
没有处理过, 进行相应处理, 返回结果. 注意, 为了幂等友好, 一定要先查询一下, 是否处理过该笔业务,
不查询直接插入业务系统, 会报错, 但实际已经处理了.

#####################################

SpringBoot常用注解

1、@SpringBootApplication 注解
@SpringBootConfiguration: 标注当前类是配置类, 这个注解继承自@Configuration.
并会将当前类内声明的一个或多个以@Bean注解标记的方法的实例纳入到srping容器中, 并且实例名就是方法名.

@EnableAutoConfiguration: 是自动配置的注解, 这个注解会根据我们添加的组件jar来完成一些默认配置,
我们做微服时会添加spring-boot-starter-web这个组件jar的pom依赖, 这样配置会默认配置springmvc 和tomcat

@ComponentScan: 扫描当前包及其子包下被@Component, @Controller, @Service, @Repository
注解标记的类并纳入到spring容器中进行管理. 等价于<context:component-scan>的xml配置文件中的配置项.

2、@ServletComponentScan: Servlet、Filter、Listener 可以直接通过
@WebServlet、@WebFilter、@WebListener 注解自动注册, 这样通过注解servlet , 拦截器, 监听器的功能而无需其他配置,
所以这次相中使用到了filter的实现, 用到了这个注解.

3、@MapperScan: spring-boot支持mybatis组件的一个注解, 通过此注解指定mybatis接口类的路径, 即可完成对mybatis接口的扫描.

4、资源导入注解: @ImportResource @Import @PropertySource 这三个注解都是用来导入自定义的一些配置文件.


controller 层:
1、@Controller 表明这个类是一个控制器类, 和@RequestMapping来配合使用拦截请求,
如果不在method中注明请求的方式, 默认是拦截get和post请求. 这样请求会完成后转向一个视图解析器.
但是在大多微服务搭建的时候, 前后端会做分离.所以请求后端只关注数据处理,
后端返回json数据的话, 需要配合@ResponseBody注解来完成.

@RestController 是@Controller 和@ResponseBody的结合, 一个类被加上@RestController 注解,
数据接口中就不再需要添加@ResponseBody, 更加简洁.

同样的情况, @RequestMapping(value="",method= RequestMethod.GET ), 我们都需要明确请求方式.
这样的写法又会显得比较繁琐, 于是乎就有了如下的几个注解.

普通风格	Rest风格
@RequestMapping(value=“”,method = RequestMethod.GET)
@GetMapping(value =“”)

@RequestMapping(value=“”,method = RequestMethod.POST)
@PostMapping(value =“”)

@RequestMapping(value=“”,method = RequestMethod.PUT)
@PutMapping(value =“”)

@RequestMapping(value=“”,method = RequestMethod.DELETE)
@DeleteMapping(value =“”)

2、@CrossOrigin: @CrossOrigin(origins = "", maxAge = 1000) 这个注解主要是为了解决跨域访问的问题.
这个注解可以为整个controller配置启用跨域, 也可以在方法级别启用.

3、@Autowired: 这是个最熟悉的注解, 是spring的自动装配, 这个个注解可以用到
构造器, 变量域，方法, 注解类型上. 当我们需要从bean 工厂中获取一个bean时,
Spring会自动为我们装配该bean中标记为@Autowired的元素.

4、@EnablCaching@EnableCaching: 这个注解是spring framework中的注解驱动的缓存管理功能.
自spring版本3.1起加入了该注解. 其作用相当于spring配置文件中的cache manager标签.

5、@PathVariable: 路径变量注解, @RequestMapping中用{}来定义url部分的变量名.


servcie 层:
1、@Service: 这个注解用来标记业务层的组件, 我们会将业务逻辑处理的类都会加上这个注解交给spring容器.
事务的切面也会配置在这一层. 当让 这个注解不是一定要用, 有个泛指组件的注解,
当我们不能确定具体作用的时候 可以用泛指组件的注解托付给spring容器.

2、@Resource: @Resource和@Autowired一样都可以用来装配bean, 都可以标注字段上, 或者方法上.
@resource注解不是spring提供的, 是属于J2EE规范的注解.
@Resource默认按照名称方式进行bean匹配, @Autowired默认按照类型方式进行bean匹配.


持久层:
1、@Repository: @Repository注解类作为DAO对象, 管理操作数据库的对象.
@Component, @Service, @Controller, @Repository是spring注解,
注解后可以被spring框架所扫描并注入到spring容器来进行管理.
@Component是通用注解，其他三个注解是这个注解的拓展，并且具有了特定的功能。

2、@Transactional: 通过这个注解可以声明事务, 可以添加在类上或者方法上.
在spring boot中 不用再单独配置事务管理, 一般情况是我们会在servcie层添加了事务注解,
即可开启事务. 要注意的是, 事务的开启只能在public 方法上. 并且主要事务切面的回滚条件.
正常我们配置rollbackfor exception时, 如果在方法里捕获了异常就会导致事务切面配置的失效.


其他相关注解:
@ControllerAdvice 和 @RestControllerAdvice: 通常和@ExceptionHandler、@InitBinder、@ModelAttribute一起配合使用.
@ControllerAdvice 和 @ExceptionHandler 配合完成统一异常拦截处理.
@RestControllerAdvice 是 @ControllerAdvice 和 @ResponseBody的合集, 可以将异常以json的格式返回数据.

#####################################

Springboot项目全局异常统一处理:
1. SpringBoot中有一个ControllerAdvice的注解, 使用该注解表示开启了全局异常的捕获,
我们只需在自定义多个方法使用ExceptionHandler注解然后定义捕获异常的类型即可对这些捕获的异常进行统一的处理.
可以针对不同的异常给出相应的返回信息.
方法注解:
@ExceptionHandler(value = BizException.class)
@ExceptionHandler(value = NullPointerException.class)
@ExceptionHandler(value = Exception.class)


2. 基于切面实现一个异常统一处理


#####################################

@ConditionalOnBean 与Bean注册优先级
第一梯队如下:
1.1. 扫描的Bean, 包括直接扫描、自动配置中扫描、启用组件中扫描的Bean:
    即扫描标注了 @Controller @Service @Repository @Component 的类
1.2. 直接的Java配置, 即@SpringBootApplication直接覆盖的包下用@Bean注册的Bean

第二梯队如下：
2.1. 启用组件中的Java配置注册的Bean, 即在@Enable...引入、而且使用@Bean注册的Bean

第三梯队如下：
3.1. 自动配置中的Java配置注册的Bean, 即在自动配置中引入、而且使用@Bean注册的Bean

