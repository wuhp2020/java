当你安装Docker时, 它会自动创建三个网络.
你可以使用以下docker network ls命令列出这些网络.

我们在使用docker run创建Docker容器时, 可以用 --net 选项指定容器的网络模式, Docker可以有以下4种网络模式:
host模式: 使用 --net=host 指定.
none模式: 使用 --net=none 指定.
bridge模式: 使用 --net=bridge 指定, 默认设置.
container模式: 使用 --net=container:NAME_or_ID 指定.


1. Host模式
相当于Vmware中的桥接模式, 与宿主机在同一个网络中, 但没有独立IP地址.
众所周知, Docker使用了Linux的Namespaces技术来进行资源隔离, 如PID Namespace隔离进程,
Mount Namespace隔离文件系统, Network Namespace隔离网络等.
一个Network Namespace提供了一份独立的网络环境, 包括网卡、路由、Iptable规则等都与其他的Network Namespace隔离.
一个Docker容器一般会分配一个独立的Network Namespace, 但如果启动容器的时候使用host模式,
那么这个容器将不会获得一个独立的Network Namespace, 而是和宿主机共用一个Network Namespace.
容器将不会虚拟出自己的网卡, 配置自己的IP等, 而是使用宿主机的IP和端口.

2. Container模式
在理解了host模式后, 这个模式也就好理解了. 这个模式指定新创建的容器和已经存在的一个容器共享一个Network Namespace,
而不是和宿主机共享. 新创建的容器不会创建自己的网卡, 配置自己的IP, 而是和一个指定的容器共享IP、端口范围等.
同样, 两个容器除了网络方面, 其他的如文件系统、进程列表等还是隔离的. 两个容器的进程可以通过lo网卡设备通信.

3. None模式
该模式将容器放置在它自己的网络栈中, 但是并不进行任何配置. 实际上, 该模式关闭了容器的网络功能,
在以下两种情况下是有用的:
容器并不需要网络（例如只需要写磁盘卷的批处理任务）.

4. Bridge模式
相当于Vmware中的Nat模式, 容器使用独立network Namespace, 并连接到docker0虚拟网卡（默认模式）.
通过docker0网桥以及Iptables nat表配置与宿主机通信, bridge模式是Docker默认的网络设置,
此模式会为每一个容器分配Network Namespace、设置IP等, 并将一个主机上的Docker容器连接到一个虚拟网桥上.


#####################################

bridge驱动实现机制:

1. 安装docker, 生成docker0网桥
当在一台未经过特殊网络配置的centos 或 ubuntu机器上安装完docker之后,
在宿主机上通过ifconfig命令可以看到多了一块名为docker0的网卡, 假设IP为 172.17.0.1/16. 有了这样一块网卡,
宿主机也会在内核路由表上添加一条到达相应网络的静态路由, 可通过route -n查看.
此条路由表示所有目的IP地址为172.17.0.0/16的数据包从docker0网卡转发.

2. 启动容器, 默认网络模式
然后使用docker run命令创建一个执行shell(/bin/bash)的Docker容器, 假设容器名称为con1.
在con1容器中可以看到它有两个网卡lo和eth0, lo设备不必多说, 是容器的回环网卡.
eth0即为容器与外界通信的网卡, eth0的ip 为 172.17.0.2/16, 和宿主机上的网桥docker0在同一个网段.
查看con1的路由表, 可以发现con1的默认网关正是宿主机的docker0网卡, 通过测试,
con1可以顺利访问外网和宿主机网络, 因此表明con1的eth0网卡与宿主机的docker0网卡是相互连通的.

这时在来查看(ifconfig)宿主机的网络设备, 会发现有一块以“veth”开头的网卡, 如veth60b16bd,
我们可以大胆猜测这块网卡肯定是veth设备了, 而veth pair总是成对出现的, veth pair通常用来连接两个network namespace,
那么另一个应该是Docker容器con1中的eth0了. 之前已经判断con1容器的eth0和宿主机的docker0是相连的,
那么veth60b16bd也应该是与docker0相连的, 不难想到, docker0就不只是一个简单的网卡设备了, 而是一个网桥.

真实情况正是如此, 创建了docker0网桥, 并以eth pair连接各容器的网络, 容器中的数据通过docker0网桥转发到eth0网卡上.
在Linux中, 可以使用brctl命令查看和管理网桥.

3. iptables规则
Docker安装完成后, 将默认在宿主机系统上增加一些iptables规则, 以用于Docker容器和容器之间以及和外界的通信,
可以使用iptables-save命令查看. 其中nat表中的POSTROUTING链有这么一条规则:
-A POSTROUTING -s 172.17.0.0/16 ! -o docker0 -j MASQUERADE
参数说明:
-s: 源地址172.17.0.0/16
-o: 指定数据报文流出接口为docker0
-j: 动作为MASQUERADE（地址伪装）

上面这条规则关系着Docker容器和外界的通信, 含义是：将源地址为172.17.0.0/16的数据包（即Docker容器发出的数据）,
当不是从docker0网卡发出时做SNAT. 这样一来, 从Docker容器访问外网的流量, 在外部看来就是从宿主机上发出的,
外部感觉不到Docker容器的存在.

那么, 外界想到访问Docker容器的服务时该怎么办呢? 我们启动一个简单的web服务容器, 观察iptables规则有何变化.
(1) 首先启动一个 tomcat容器, 将其8080端口映射到宿主机上的8080端口上:
docker run -itd --name  tomcat01 -p 8080:8080 tomcat:latest

(2) 然后查看iptabels规则:
iptables-save

*nat
-A POSTROUTING -s 172.17.0.4/32 -d 172.17.0.4/32 -p tcp -m tcp --dport 8080 -j MASQUERADE
...
*filter
-A DOCKER -d 172.17.0.4/32 ! -i docker0 -o docker0 -p tcp -m tcp --dport 8080 -j ACCEPT

可以看到, 在nat、filter的Docker链中分别增加了一条规则,
这两条规则将访问宿主机8080端口的流量转发到了172.17.0.4的8080端口上（真正提供服务的Docker容器IP和端口）,
所以外界访问Docker容器是通过iptables做DNAT（目的地址转换）实现的.





