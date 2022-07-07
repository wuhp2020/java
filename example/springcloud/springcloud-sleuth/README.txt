核心原理: 自动装配 TraceWebServletAutoConfiguration , 创建filter, 实现类TracingFilter, 实现过滤

#####################################

Spring Cloud Sleuth + Zipkin原理分析:
Spring Cloud Sleuth可以追踪以下类型的组件:
async, hystrix, messaging, websocket, rxjava, scheduling, web（SpringWebMvc, Spring WebFlux, Servlet）,
webclient（Spring RestTemplate）, feign, zuul.
通过spring-cloud-sleuth-core的jar包结构, 可以很明显的看出, sleuth支持链路追踪的组件（web下面包括http、client和feign）:

web:
webflux通过注册TraceWebFilter, webmvc通过实现HandlerInterceptorAdapter,
Servlet通过定义AOP切面对@RestController、@Controller、Callable对请求进行trace拦截,
完成span的新建、传递和销毁. 可以设置spring.sleuth.web.enabled为false禁用所有web请求的sleuth跟踪.

async:
通过TraceAsyncAspect对@Async注解进行拦截, 通过 TraceRunnable 和 TraceCallable来对
runnable和callable进行包装和利用LazyTraceExecutor来代替java的Executor. Spring Cloud Sleuth利用以上方式进行span的新建和销毁.
如果需要禁用的话, 可以设置spring.sleuth.async.enabled为false. 如果禁用, 与异步相关的机制就不会发生.

hystrix:
原理是使用HystrixPlugins添加trace相关的plugin, 自定义了一个HystrixConcurrencyStrategy子类SleuthHystrixConcurrencyStrategy.
若需要禁用可以设置spring.sleuth.hystrix.strategy.enable为false.

messaging:
Spring Cloud Sleuth提供了TracingChannelInterceptor,
是基于Spring message的ChannelInterceptorAdapter/ExecutorChannelInterceptor,
它发布/订阅事件都是会进行span的新建和销毁的. 可以设置spring.sleuth.integration.enabled为false禁用该机制.

websocket:
将TracingChannelInterceptor拦截类注册到ChannelRegistration中进行trace拦截.

rxjava:
通过自定义RxJavaSchedulersHook的子类SleuthRxJavaSchedulersHook, 它使用TraceAction来包装实例中Action0.
这个钩子对象, 会根据之前调度的Action是否已经开始跟踪, 来决定是创建还是延续使用span.
可以通过设置spring.sleuth.rxjava.schedulers.hook.enabled为false来关闭这个对象的使用.
可以定义一组正则表达式来对线程名进行过滤, 来选择哪些线程不需要跟踪.

scheduling:
原理是建立TraceSchedulingAspect 切面对Scheduled注解进行trace拦截, 对span进行创建和销毁.
可以通过设置spring.sleuth.scheduled.enabled为false来使该切面无效.

feign:
Spring Cloud Sleuth默认通过TraceFeignClientAutoConfiguration提供feign的集成,
可以设置spring.sleuth.feign.enabled为false来使其无效.

zuul:
注册Zuul过滤器TracePostZuulFilter来传递tracing信息(请求头使用tracing数据填满),
可以设置spring.sleuth.zuul.enabled为false来关闭Zuul服务.

docker run -d -p 9411:9411 openzipkin/zipkin

#####################################

Spring Cloud Sleuth原理:
Span: 基本工作单元, 发送一个远程调度任务 就会产生一个Span, Span是一个64位ID唯一标识的,
Trace是用另一个64位ID唯一标识的, Span还有其他数据信息, 比如摘要、时间戳事件、Span的ID、以及进度ID.

Trace: 一系列Span组成的一个树状结构, 请求一个微服务系统的API接口, 这个API接口, 需要调用多个微服务,
调用每个微服务都会产生一个新的Span, 所有由这个请求产生的Span组成了这个Trace.

Annotation: 用来及时记录一个事件的, 一些核心注解用来定义一个请求的开始和结束. 这些注解包括以下:
cs - Client Sent - 客户端发送一个请求, 这个注解描述了这个Span的开始
sr - Server Received - 服务端获得请求并准备开始处理它, 如果将其sr减去cs时间戳便可得到网络传输的时间.
ss - Server Sent （服务端发送响应）– 该注解表明请求处理的完成(当请求返回客户端),如果ss的时间戳减去sr时间戳, 就可以得到服务器请求的时间.
cr - Client Received （客户端接收响应）- 此时Span的结束, 如果cr的时间戳减去cs时间戳便可以得到整个请求所消耗的时间.


1. 用户调用client时，client如何生成Span信息
2. client调用server时，如何将Span发送到server
3. server如何接收cliend的Span信息
4. client，server如何发送Span到zipkin

由自定义filter实现:
问题1和问题3:
都由LazyTracingFilter处理, 它在TraceWebServletAutoConfiguration中初始化
LazyTracingFilter#doFilter -> (brave)TracingFilter#doFilter

问题2:
LazyTracingClientHttpRequestInterceptor负责实现该功能. 它在TraceWebClientAutoConfiguration中构建.
LazyTracingClientHttpRequestInterceptor#interceptor -> TracingClientHttpRequestInterceptor#intercept(brave)

问题4:
在Spring Cloud Sleuth中,
TracingClientHttpRequestInterceptor#intercept方法'#4'步骤 -> RealSpan#finish -> ZipkinFinishedSpanHandler#handle.
ZipkinFinishedSpanHandler#handle会调用Reporter#report来上报Span数据.
ZipkinFinishedSpanHandler是Tracing默认的FinishedSpanHandler.

