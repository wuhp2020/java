核心原理:
@EnableFeignClients中import导入FeignClientsRegistrar上报bean,
@Autowired时getObject()获取代理类 FeignClientFactoryBean,
调用工厂getObject(), 到HystrixTargeter, 再到ReflectiveFeign, 完成代理

#####################################

Feign 的启动原理:

1. 启动某个组件:
需要在类上标记此注解 @EnableFeignClients
继续深入看一下注解内部都做了什么, 前三个注解看着平平无奇, 重点在第四个 @Import 上, 一般使用此注解都是想要动态注册Spring Bean的
ResourceLoaderAware、EnvironmentAware 为 FeignClientsRegistrar 中两个属性 resourceLoader、environment 赋值,
ImportBeanDefinitionRegistrar 负责动态注入 IOC Bean, 分别注入 Feign 配置类、FeignClient Bean

2. 添加全局配置:
registerDefaultConfiguration 方法流程如下

获取 @EnableFeignClients 注解上的属性以及对应 Value
生成 FeignClientSpecification（存储 Feign 中的配置类） 对应的构造器 BeanDefinitionBuilder
FeignClientSpecification Bean 名称为 default + @EnableFeignClients 修饰类全限定名称 + FeignClientSpecification
@EnableFeignClients defaultConfiguration 默认为 {},
如果没有相关配置，默认使用 FeignClientsConfiguration 并结合 name 填充到 FeignClientSpecification, 最终注册为 IOC Bean

3. 注册 FeignClient 接口:
将重点放在 registerFeignClients 上, 该方法主要就是将修饰了 @FeignClient 的接口注册为 IOC Bean

扫描 @EnableFeignClients 注解, 如果有 clients, 则加载指定接口, 为空则根据 scanner 规则扫描出修饰了 @FeignClient 的接口
获取 @FeignClient 上对应的属性, 根据 configuration 属性去创建接口级的 FeignClientSpecification 配置类 IOC Bean
将 @FeignClient 的属性设置到 FeignClientFactoryBean 对象上, 并注册 IOC Bean
@FengnClient 修饰的接口实际上使用了 Spring 的代理工厂生成代理类,
所以这里会把修饰了 @FeignClient 接口的 BeanDefinition 设置为 FeignClientFactoryBean 类型,
而 FeignClientFactoryBean 继承自 FactoryBean

也就是说, 当我们定义 @FeignClient 修饰接口时, 注册到 IOC 容器中 Bean 类型变成了 FeignClientFactoryBean

在 Spring 中, FactoryBean 是一个工厂 Bean, 用来创建代理 Bean.
工厂 Bean 是一种特殊的 Bean, 对于需要获取 Bean 的消费者而言, 它是不知道 Bean 是普通 Bean 或是工厂 Bean 的.
工厂 Bean 返回的实例不是工厂 Bean 本身, 而是会返回执行了工厂 Bean 中 FactoryBean#getObject 逻辑的实例.


#####################################

Feign 的工作原理:
上面说到 @FeignClient 修饰的接口最终填充到 IOC 容器的类型是 FeignClientFactoryBean

1. FeignClientFactoryBean 都有哪些特征:
它会在类初始化时执行一段逻辑, 依据 Spring InitializingBean 接口
如果它被别的类 @Autowired 进行注入, 返回的不是它本身, 而是 FactoryBean#getObject 返回的类, 依据 Spring FactoryBean 接口
它能够获取 Spring 上下文对象, 依据 Spring ApplicationContextAware 接口
重点以及关键就是 FactoryBean#getObject 方法
feign 方法里日志工厂、编码、解码等类均是通过 get(...) 方法得到

到这里有必要总结一下创建 Spring 代理工厂的前半场代码:
注入@FeignClient 服务时, 其实注入的是 FactoryBean#getObject 返回代理工厂对象
通过 IOC 容器获取 FeignContext 上下文
创建 Feign.Builder 对象时会创建 Feign 服务对应的子容器
从子容器中获取日志工厂、编码器、解码器等 Bean
为 Feign.Builder 设置配置, 比如超时时间、日志级别等属性, 每一个服务都可以个性化设置

2. 动态代理生成
因为我们在 @FeignClient 注解是使用 name 而不是 url, 所以会执行负载均衡策略的分支
Client: Feign 发送请求以及接收响应等都是由 Client 完成, 该类默认 Client.Default
进入实现类 HystrixTargeter , 因为我们并没有对 Hystix 进行设置,
创建反射类 ReflectiveFeign，然后执行创建实例类
newInstance 方法对 @FeignClient 修饰的接口中 SpringMvc 等配置进行解析转换, 对接口类中的方法进行归类, 生成动态代理类

根据 newInstance 方法按照行为大致划分, 共做了四件事:
处理 @FeignCLient 注解（SpringMvc 注解等）封装为 MethodHandler 包装类
遍历接口中所有方法, 过滤 Object 方法, 并将默认方法以及 FeignClient 方法分类
创建动态代理对应的 InvocationHandler 并创建 Proxy 实例
接口内 default 方法 绑定动态代理类
MethodHandler 将方法参数、方法返回值、参数集合、请求类型、请求路径进行解析存储

也就是说在我们调用 @FeignClient 接口时, 会被 FeignInvocationHandler#invoke 拦截, 并在动态代理方法中执行下述逻辑
接口注解信息封装为 HTTP Request
通过 Ribbon 获取服务列表, 并对服务列表进行负载均衡调用（服务名转换为 ip+port）
请求调用后, 将返回的数据封装为 HTTP Response, 继而转换为接口中的返回类型


#####################################