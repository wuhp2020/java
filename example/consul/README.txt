安装
docker run -itd --name=consul -p 8500:8500 consul:latest agent -server -bootstrap -ui -node=1 -client='0.0.0.0'

#####################################

consul模式:
CLIENT表示consul的client模式, 就是客户端模式. 是consul节点的一种模式, 这种模式下,
所有注册到当前节点的服务会被转发到SERVER, 本身是不持久化这些信息. 简单的说, client处理健康检查, 注册服务等,
但是这个注册只是转发到server中, 如果有成千上万的服务, 分别启动多个client, 可以减少server压力

SERVER表示consul的server模式, 表明这个consul是个server, 这种模式下, 功能和CLIENT都一样, 唯一不同的是,
它会把所有的信息持久化的本地, 这样遇到故障, 信息是可以被保留的

SERVER-LEADER中间那个SERVER下面有LEADER的字眼, 表明这个SERVER是它们的老大, 它和其它SERVER不一样的一点是,
它需要负责同步注册的信息给其它的SERVER, 同时也要负责各个节点的健康监测

#####################################

consul原理:
1. 服务器Server1、Server2、Server3上分别部署了Consul Server, 组成了Consule集群,
通过raft选举算法, server2成为leader节点

2. 服务器Server4和Server5上通过Consul Client分别注册Service A、B、C,
服务A、B、C注册到Consul可以通过HTTP API（8500 端口）的方式, 也可以通过Consul配置文件的方式

3. Consul Client将注册信息通过RPC转发到Consul Server, 服务信息保存在Server的各个节点中, 并且通过raft实现了强一致性

4. 服务器Server6中Program D要访问Service B, 此时Program D先访问本机Consul Client提供的HTTP API,
Consul Client会将请求转发到Consul Server, Consul Server查询到Service B并返回,
最终Program D拿到了Service B的所有部署的IP和端口, 根据负载均衡策略, 选择Service B的其中一个并向其发起请求

如果服务发现采用的是DNS方式, 则Program D中使用Service B的服务发现域名, 域名解析请求首先到达本机DNS代理,
然后转发到本机Consul Client, consul Client会将请求转发到Consul Server, 随后的流程和上述一直

#####################################

consul参数
-bootstrap-expect
此标志提供数据中心中预期服务器的数量, 不应该提供此值, 或者该值必须与群集中的其他服务器一致.
提供时, Consul会等待指定数量的服务器可用, 然后引导群集, 这允许初始领导者自动选举.
这不能与遗留-bootstrap标志结合使用, 该标志需要-server模式

