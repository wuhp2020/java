spring原理:
内部最核心的就是IOC了, 动态注入, 让一个对象的创建不用new了, 可以自动的生产,
这其实就是利用java里的反射, 反射其实就是在运行时动态的去创建、调用对象,
Spring就是在运行时, 跟xml Spring的配置文件来动态的创建对象, 和调用对象里的方法的.

Spring还有一个核心就是AOP这个就是面向切面编程, 可以为某一类对象 进行监督和控制
（也就是 在调用这类对象的具体方法的前后去调用你指定的 模块）从而达到对一个模块扩充的功能.
这些都是通过  配置类达到的.

Spring目的: 就是让对象与对象（模块与模块）之间的关系没有通过代码来关联,
都是通过配置类说明管理的（Spring根据这些配置 内部通过反射去动态的组装对象）

要记住: Spring是一个容器, 凡是在容器里的对象才会有Spring所提供的这些服务和功能.
Spring里用的最经典的一个设计模式就是: 模板方法模式(这里我都不介绍了，是一个很常用的设计模式),
Spring里的配置是很多的, 很难都记住, 但是Spring里的精华也无非就是以上的两点,
把以上两点跟理解了 也就基本上掌握了Spring.

#####################################

Spring常用的三种注入方式:

Spring通过DI（依赖注入）实现IOC（控制反转）,
1. 构造方法注入:
@Service
public class AService {
    BService bService;
    @Autowired
    public AService(BService bService) {
        this.bService = bService;
    }
}


2. setter注入:
@Service
public class BService {
    AService aService;

    @Autowired
    public void setaService(AService aService) {
        this.aService = aService;
    }
}
上面这两种写法都可以, spring会将name值的每个单词首字母转换成大写,
然后再在前面拼接上”set”构成一个方法名, 然后去对应的类中查找该方法, 通过反射调用, 实现注入.

还有一点需要注意: 如果通过set方法注入属性, 那么spring会通过默认的空参构造方法来实例化对象,
所以如果在类中写了一个带有参数的构造方法, 一定要把空参数的构造方法写上, 否则spring没有办法实例化对象, 导致报错.

3. 属性注入
在介绍注解注入的方式前, 先简单了解bean的一个属性autowire,
autowire主要有三个属性值: constructor, byName, byType
@Service
public class BService {
    @Autowired
    AService aService;
    //...
}

#####################################

Spring怎么解决循环依赖:
Spring的循环依赖的理论依据基于Java的引用传递, 当获得对象的引用时,
对象的属性是可以延后设置的.（但是构造器必须是在获取引用之前）

（1）createBeanInstance：实例化，其实也就是调用对象的构造方法实例化对象
（2）populateBean：填充属性，这一步主要是多bean的依赖属性进行填充
（3）initializeBean：调用spring xml中的init 方法。

从上面单例bean的初始化可以知道: 循环依赖主要发生在第一、二步,
也就是构造器循环依赖和field循环依赖. 那么我们要解决循环引用也应该从初始化过程着手,
对于单例来说, 在Spring容器整个生命周期内, 有且只有一个对象, 所以很容易想到这个对象应该存在Cache中,
Spring为了解决单例的循环依赖问题, 使用了三级缓存
这三级缓存分别指:
　　singletonFactories: 单例对象工厂的cache
　　earlySingletonObjects: 提前暴光的单例对象的Cache
　　singletonObjects: 单例对象的cache

循环依赖案例:
"A的某个field或者setter依赖了B的实例对象, 同时B的某个field或者setter依赖了A的实例对象"这种循环依赖的情况.
A首先完成了初始化的第一步, 并且将自己提前曝光到singletonFactories中,
此时进行初始化的第二步, 发现自己依赖对象B, 此时就尝试去get(B), 发现B还没有被create,
所以走create流程, B在初始化第一步的时候发现自己依赖了对象A, 于是尝试get(A),
尝试一级缓存singletonObjects(肯定没有，因为A还没初始化完全), 尝试二级缓存earlySingletonObjects（也没有）,
尝试三级缓存singletonFactories, 由于A通过ObjectFactory将自己提前曝光了,
所以B能够通过ObjectFactory.getObject拿到A对象(虽然A还没有初始化完全，但是总比没有好呀),
B拿到A对象后顺利完成了初始化阶段1、2、3，完全初始化之后将自己放入到一级缓存singletonObjects中,
此时返回A中, A此时能拿到B的对象顺利完成自己的初始化阶段2、3, 最终A也完成了初始化,
进去了一级缓存singletonObjects中, 而且更加幸运的是, 由于B拿到了A的对象引用,
所以B现在hold住的A对象完成了初始化.

知道了这个原理时候, 肯定就知道为啥Spring不能解决"A的构造方法中依赖了B的实例对象,
同时B的构造方法中依赖了A的实例对象"这类问题了!
因为加入singletonFactories三级缓存的前提是执行了构造器, 所以构造器的循环依赖没法解决

#####################################

事务失效的几种原因分析:
1、spring的事务注解@Transactional只能放在public修饰的方法上才起作用,
如果放在其他非public（private，protected）方法上, 虽然不报错, 但是事务不起作用

2、如果采用spring+spring mvc, 则context:component-scan重复扫描问题可能会引起事务失败

3、如使用mysql且引擎是MyISAM, 则事务会不起作用, 原因是MyISAM不支持事务, 可以改成InnoDB引擎

4、@Transactional 注解开启配置, 必须放到listener里加载,
如果放到DispatcherServlet的配置里, 事务也是不起作用的

6、在业务代码中如果抛出RuntimeException异常, 事务回滚, 但是抛出Exception, 事务不回滚

7、如果在加有事务的方法内, 使用了try...catch..语句块对异常进行了捕获,
而catch语句块没有throw new RuntimeExecption异常, 事务也不会回滚

8、在类A里面有方法a 和方法b, 然后方法b上面用 @Transactional加了方法级别的事务,
在方法a里面 调用了方法b, 方法b里面的事务不会生效.
原因是在同一个类之中, 方法互相调用, 切面无效, 而不仅仅是事务.
这里事务之所以无效, 是因为spring的事务是通过aop实现的

#####################################

Aware接口作用:
Spring框架提供了多个*Aware接口, 用于辅助Spring Bean以编程的方式调用Spring容器.
通过实现这些接口, 可以增强Spring Bean的功能, 但是也会造成对Spring容器的绑定.
Spring框架启动时, ApplicationContext 初始化实现了Aware接口的Spring Bean时,
并将 ApplicationContext 的引用作为参数传递给创建的Spring Bean实例,
创建的Spring Bean实例 通过 ApplicationContext 的引用操作 Spring 框架的各种资源

Aware接口	                 说明
ApplicationContextAware        :能获取Application Context调用容器的服务. 可以在Bean中得到Bean所在的应用上下文, 从而直接在Bean中使用上下文的服务
ApplicationEventPublisherAware :应用事件发布器, 在bean中可以得到应用上下文的事件发布器, 从而可以在Bean中发布应用上下文的事件
BeanClassLoaderAware           :能获取加载当前Bean的类加载器
BeanFactoryAware	           :可以在Bean中得到Bean所在的IOC容器, 从而直接在Bean中使用IOC容器的服务
BeanNameAware	               :可以在Bean中得到它在IOC容器中的Bean的实例的名字
EnvironmentAware	           :能获取当前容器的环境属性信息
MessageSourceAware	           :在Bean中可以得到消息源、国际化文本信息
NotificationPublisherAware	   :JMX通知
ResourceLoaderAware	           :在Bean中可以得到ResourceLoader, 获取资源加载器读取资源文件
ServletConfigAware	           :能获取到ServletConfig
ServletContextAware	           :能获取到ServletContext

#####################################

Spring Bean的生命周期:
1. 实例化 Instantiation
2. 属性赋值 Populate
3. 初始化 Initialization
4. 销毁 Destruction

createBeanInstance() -> 实例化
populateBean() -> 属性赋值
initializeBean() -> 初始化

// 忽略了无关代码
protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)
      throws BeanCreationException {

   // Instantiate the bean.
   BeanWrapper instanceWrapper = null;
   if (instanceWrapper == null) {
       // 实例化阶段！
      instanceWrapper = createBeanInstance(beanName, mbd, args);
   }

   // Initialize the bean instance.
   Object exposedObject = bean;
   try {
       // 属性赋值阶段！
      populateBean(beanName, mbd, instanceWrapper);
       // 初始化阶段！
      exposedObject = initializeBean(beanName, exposedObject, mbd);
   }
}

#####################################

BeanFactory 和 FactoryBean 的区别
BeanFactory:
IOC 容器, 并且提供方法支持外部程序对这些 bean 的访问, 在程序启动时 根据传入的参数产生各种类型的 bean,
并添加到 IOC容器（实现 BeanFactory 接口的类) 的 singletonObject 属性中.
XmlBeanFactory
ApplicationContext

FactoryBean:
有工厂的味道
首先是个 bean, 也存放在 BeanFactory 中. 它具有工厂方法的功能,
在程序运行中 产生指定(一种)类型的 bean, 并添加到了 IOC容器中的 factoryBeanObjectCache 属性中.