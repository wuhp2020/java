package com;

//import com.wuhp.annotation.EnableModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @SpringBootConfiguration
 * 组合了@Configuration注解, 实现配置文件的功能
 *
 * @EnableAutoConfiguration
 * 打开自动配置的功能, 也可以关闭某个自动配置的选项,
 * 如关闭数据源自动配置功能: @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
 * Spring启动的时候会扫描所有jar路径下的META-INF/spring.factories, 将其文件包装成Properties对象
 *
 * @ComponentScan
 * Spring组件扫描
 */
@EnableScheduling
@SpringBootApplication
//@EnableModule                       // 手动装配, 不用META-INF/spring.factories
//@Target(ElementType.TYPE)           // 注解的适用范围, 其中TYPE用于描述类、接口（包括包注解类型）或enum声明
//@Retention(RetentionPolicy.RUNTIME) // 注解的生命周期, 保留到class文件中（三个生命周期）
//@Documented                         // 表明这个注解应该被javadoc记录
//@Inherited                          // 子类可以继承该注解
//@SpringBootConfiguration            // 继承了Configuration, 表示当前是注解类
//@EnableAutoConfiguration            // 开启springboot的注解功能, springboot的四大神器之一, 其借助@import的帮助
//@ComponentScan(excludeFilters = {   // 扫描路径设置
//@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
//@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public class SpringBootFWApplication {

    public static void main(String[] args) {

        /**
         * 启动Tomcat就是在第7步中"刷新上下文", Tomcat的启动主要是初始化2个核心组件,
         * 连接器(Connector)和容器（Container）, 一个Tomcat实例就是一个Server,
         * 一个Server包含多个Service, 也就是多个应用程序, 每个Service包含多个连接器（Connetor）
         * 和一个容器（Container), 而容器下又有多个子容器, 按照父子关系分别为: Engine,Host,Context,Wrapper,
         * 其中除了Engine外, 其余的容器都是可以有多个
         */
        SpringApplication.run(SpringBootFWApplication.class, args);

    }
}
