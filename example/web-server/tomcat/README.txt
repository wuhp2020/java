tomcat最顶层容器是Server, 代表着整个服务器, 一个Server包含多个Service.
除Service主要包括多个Connector和一个Container.Connector用来处理连接相关的事情,
并提供Socket到Request和Response相关转化.Container用于封装和管理Servlet,以及处理具体的Request请求.

一个tomcat只包含一个Server, 一个Server可以包含多个Service, 一个Service只有一个Container,
但有多个Connector, 这样一个服务可以处理多个连接. 多个Connector和一个Container就形成了一个Service,
有了Service就可以对外提供服务了, 但是Service要提供服务又必须提供一个宿主环境, 那就非Server莫属了,
所以整个tomcat的声明周期都由Server控制.

#####################################



