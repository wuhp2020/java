JDK 动态代理能否对类代理:
因为 JDK 动态代理生成的代理类, 会继承 Proxy 类, 由于 Java 无法多继承, 所以无法对类进行代理

抽象类是否可以 JDK 动态代理:
不可以, 抽象类本质上也是类, Proxy 生成代理类过程中, 会校验传入 Class 是否接口

Mybatis Mapper 接口没有实现类, 怎么实现的动态代理:
Mybatis 会通过 Class.forname 得到 Mapper 接口 Class 对象,
生成对应的动态代理对象, 核心业务处理都会在 InvocationHandler.invoke 进行处理

#####################################

SpringBoot整合MyBatis原理
1. SpringBoot提供了MyBatis的自动配置类MybatisAutoConfiguration,
可以自动注册SqlSessionFactory、SqlSessionTemplate等组件, 开发人员只需在配置文件中指定相关属性即可.

(1) SpringBoot为我们自动注册了相应的组件：
SqlSessionFactoryBean: 用于构建MyBatis的SqlSessionFactory
SqlSessionFactoryBean是一个工厂Bean, 其作用就是加载用户自定义的配置,
然后使用MyBatis的API创建一个SqlSessionFactory

(2) SqlSessionTemplate: MyBatis的代理类, 将SqlSession与Spring的事务进行了整合
SqlSessionTemplate是Spring提供的一个对MyBatis的SqlSession的一个增强类它,
的作用就是将SqlSession与当前的事务所绑定, 而且是线程安全的, 一个SqlSessionTemplate可以被多个dao所共享.
SqlSessionTemplate基于动态代理模式, 内部委托了一个SqlSession对象, 并且在其基础上进行了增强

(3) ClassPathMapperScanner: 提供MyBatis的Mapper的自动扫描
通过@MapperScan注解, 可以指定Mapper自动扫描的包路径,
自动扫描的处理是通过ClassPathMapperScanner实现的
ClassPathMapperScanner会将所有的Mapper扫描进来,
并且将每个Mapper包装成一个类型为MapperFactoryBean的BeanDefinition, 注册到IoC容器中.
MapperScannerConfigurer实现了BeanDefinitionRegistryPostProcessor,
是一个BeanFactoryPostProcessor, 它的功能就是在容器启动阶段动态向容器中注册BeanDefinition.
经过MapperScannerConfigurer处理后,
所有Mapper接口的BeanDefinition就以MapperFactoryBean的形式注册到Spring IoC容器中了.

#####################################

springboot集成mybatis原理解读
1、在项目中引入了mybatis-spring-boot-starter依赖,
mybatis-spring-boot-starter又引进了mybatis-spring-boot-autoconfigure依赖,
在mybatis-spring-boot-autoconfigure的类路径下, 可以看到META-INF目录下有一个spring.factories文件,
文件里面的内容是org.springframework.boot.autoconfigure.EnableAutoConfiguration= \
org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration,
这里的意思是springboot项目加载时, 会扫描项目本身以及jar包类路径下的META-INF目录的spring.factories文件,
然后会加载执行文件里面所配置到的类, 同时这就是springboot自动配置的真谛所在.

2、查看MybatisAutoConfiguration类发现, 这是一个Configuration类,
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
代表在DataSourceAutoConfiguration类加载完后再加载, 这里是因为mybatis环境需要依赖数据源配置,
所以必须在数据源配置完成后才进行mybatis配置. 在MybatisAutoConfiguration类中,
配置了SqlSessionFactory类, 所在到这里mybatis的环境就已经生效了.

3、SqlSessionFactory配置完成后, 剩下的就是扫描mapper接口并生成代理类存放到IOC容器中, 这样就可以依赖注入Mapper了

4、在Application启动类中, 加上了@MapperScan(basePackages = {"com.web.mapper"})注解,
打开MapperScan注解可以发现, MapperScan加上另一个注解@Import(MapperScannerRegistrar.class),
@Import注解这里简单说明下其作用是spring环境监测到@Import注解时, 会加载其指定的配置类

5、查看MapperScannerRegistrar发现, 其实现了ImportBeanDefinitionRegistrar接口,
该接口的作用是spring会调用实现该接口的registerBeanDefinitions方法,
传入AnnotationMetadata和BeanDefinitionRegistry两个参数,
AnnotationMetadata的作用是封装了加上@Import注解的注解的属性,
这里解析的可能有点绕口, 举例说明就是@MapperScan注解加上了@Import注解,
@MapperScan有一个basePackages属性,
所以AnnotationMetadata封装了@MapperScan注解的basePackages属性的值

6、MapperScannerRegistrar的registerBeanDefinitions方法中, 获取了@MapperScan注解的属性后,
调用了自身的重载registerBeanDefinitions方法,
重载registerBeanDefinitions方法通过调用ClassPathMapperScanner的doScan方法,
就完成了Mapper的扫描并加入到spring容器中, 到了这里整个mybatis的环境就完全生效了.


#####################################

Mybatis的缓存
Mybatis的缓存分为一级缓存和二级缓存, 一级缓存是默认开启的, 二级缓存需要手动开启.
一级缓存: SqlSession级别的缓存, 作用域是一个SqlSession. 在同一个SqlSession中, 执行相同的查询sql,
第一次会先去查询数据库, 并写入缓存. 第二次再执行时, 则直接从缓存中取数据.
如果两次执行查询sql的中间执行了增删改操作, 则会清空该SqlSession的缓存.

二级缓存: mapper级别的缓存. 作用域是mapper的同一个namespace下的sql语句.
第一次执行查询SQL时, 会将查询结果存到二级缓存区域内. 第二次执行相同的查询SQL,
则直接从缓存中取出数据. 如果两次执行查询sql的中间执行了增删改操作, 则会清空该namespace下的二级缓存.


MyBatis一级缓存失效问题
我们知道MyBatis有两级缓存, 其中一级缓存是默认开启的, 但是在Spring整合了MyBatis后,
却经常出现一级缓存失效的问题, 其原因是在SqlSessionTemplate对SqlSession进行了代理后,
在非事务环境下, 每次执行完SqlSession的操作后都会进行一次commit(),
且最后后关闭SqlSession, 因此会清空一级缓存 (强调: 仅在非事务环境下, 在事务中调用不会出现这个问题)

#####################################

插件:

官方文档中并没有说明 MyBatis 插件的具体定义, 不过借助拦截器的思路, 我们还是很容易理解的:
MyBatis 的插件就是一些能拦截某些 MyBatis 核心组件方法, 增强功能的拦截器.
MyBatis 允许我们在 SQL 语句执行过程中的某些点进行拦截增强, 官方文档中列出了四种可供增强的切入点:
Executor ( update, query, flushStatements, commit, rollback, getTransaction, close, isClosed )
ParameterHandler ( getParameterObject, setParameters )
ResultSetHandler ( handleResultSets, handleOutputParameters )
StatementHandler ( prepare, parameterize, batch, update, query )
