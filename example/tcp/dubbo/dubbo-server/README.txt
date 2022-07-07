dubbo启动原理:
第一步解析:
在dubbo的jar包下, spring启动时会默认加载/MATE-INF/spring.handler文件, 该文件下有这么一段内容:
http\://code.alibabatech.com/schema/dubbo=com.alibaba.dubbo.config.spring.schema.DubboNamespaceHandler

这个DubboNamespaceHandler就是dubbo最初加载的类, 它继承了NamespaceHandlerSupport这个类,
在init()方法中, 初始化了zk和dubbo相关的配置
（application，module，registry，monitor，provider，consumer，
protocol，service，reference，annotation 这个标签）, 将dubbo生成bean交给Spring进行管理

registerBeanDefinitionParser这个方法点进去, 其实是一个HashMap, elementName是key,
new DubboBeanDefinitionParser()是value

接着DubboBeanDefinitionParser这个真正解析类

DubboBeanDefinitionParser继承了BeanDefinitionParser, 实现里面的parse方法,
这个就是dubbo注册解析的方法, 里面只有对dubbo配置文件的解析, 内有其他任何调用

第二部服务暴露:
暴露的入口在ServiceBean这个类中, 这个类实现了spring的几个接口类:
InitializingBean 当spring容器初始化完成以后, 会调用这个类的afterPropertiesSet方法,
所以说这里是dubbo暴露协议的入口

DisposableBean  bean被销毁的时候会调用destroy方法

ApplicationContextAware bean初始化成功后会注入applicationContext对象

ApplicationListener spring容器初始化完成后会触发事件监听

BeanNameAware spring初始化完以后，会获得bean的ID属性

#####################################