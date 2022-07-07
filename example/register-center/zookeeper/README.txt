修改配置:
zoo.cfg
dataDir=./data

启动命令:
./bin/zkServer.sh start

#####################################

集群配置:
格式: server.id=host:port:port
第一个port: zookeeper节点通信端口, 可随意指定, 不与其他端口冲突, 多个节点要统一
第二个port: leader选举时的通讯端口号, 可随意指定, 不与其他端口冲突, 多个节点要统一

在dataDir目录下新建一个myid文件, 里面值和zoo.cfg配置文件的server.id中的id对应,
这里的myid值主要是用于leader竞选时的标识

#####################################

leader选举过程:
假设这些服务器 myid 从 1-5, 依序启动:
1. 服务器1启动, 发起一次选举
服务器1投自己一票, 此时服务器1票数一票, 不够半数以上（3票）, 选举无法完成, 服务器1状态保持为LOOKING

2. 服务器2启动，再发起一次选举
服务器1和2分别投自己一票, 此时服务器1发现服务器2的id比自己大, 更改选票投给服务器2
此时服务器1票数0票, 服务器2票数2票, 不够半数以上（3票）, 选举无法完成, 服务器1、2状态保持LOOKING

3. 服务器3启动，发起一次选举。
与上面过程一样, 服务器1和2先投自己一票, 然后因为服务器3id最大, 两者更改选票投给为服务器3
此次投票结果: 服务器1为0票, 服务器2为0票, 服务器3为3票. 此时服务器3的票数已经超过半数（3票）, 服务器3当选Leade
服务器1、2更改状态为FOLLOWING, 服务器3更改状态为LEADING

4. 服务器4启动，发起一次选举。
此时服务器1、2、3已经不是LOOKING状态, 不会更改选票信息. 交换选票信息结果: 服务器3为3票, 服务器4为1票
此时服务器4服从多数, 更改选票信息为服务器3, 服务器4并更改状态为FOLLOWING

5. 服务器5启动, 同4一样投票给3, 此时服务器3一共5票, 服务器5为0票, 服务器5并更改状态为FOLLOWING


#####################################

zookeeper使用ACL来进行权限的控制, 包含以下5种:

CREATE，创建子节点权限
DELETE，删除子节点权限
READ，获取节点数据和子节点列表权限
WRITE，更新节点权限
ADMIN，设置节点ACL权限

所以, Zookeeper通过集群的方式来做到高可用, 通过内存数据节点Znode来达到高性能,
但是存储的数据量不能太大, 通常适用于读多写少的场景

#####################################

Zookeeper有哪些应用场景？
1. 命名服务Name Service, 依赖Zookeeper可以生成全局唯一的节点ID, 来对分布式系统中的资源进行管理
2. 分布式协调, 这是Zookeeper的核心使用了, 利用Wather的监听机制, 一个系统的某个节点状态发生改变, 另外系统可以得到通知
3. 集群管理, 分布式集群中状态的监控和管理, 使用Zookeeper来存储
4. Master选举, 利用Zookeeper节点的全局唯一性, 同时只有一个客户端能够创建成功的特点, 可以作为Master选举使用, 创建成功的则作为Master
5. 分布式锁, 利用Zookeeper创建临时顺序节点的特性

#####################################

zookeeper是如何保证数据一致性的:

Zookeeper通过ZAB原子广播协议来实现数据的最终顺序一致性, 他是一个类似2PC两阶段提交的过程
由于Zookeeper只有Leader节点可以写入数据, 如果是其他节点收到写入数据的请求, 则会将之转发给Leader节点

1. Leader收到请求之后, 将它转换为一个proposal提议, 并且为每个提议分配一个全局唯一递增的事务ID: zxid,
然后把提议放入到一个FIFO的队列中, 按照FIFO的策略发送给所有的Follower
2. Follower收到提议之后, 以事务日志的形式写入到本地磁盘中, 写入成功后返回ACK给Leader
3. Leader在收到超过半数的Follower的ACK之后, 即可认为数据写入成功,
就会发送commit命令给Follower告诉他们可以提交proposal了

#####################################

CAP理论:
一致性(Consistency) (所有节点在同一时间具有相同的数据)
可用性(Availability) (保证每个请求不管成功或者失败都有响应)
分隔容忍(Partition tolerance) (系统中任意信息的丢失或失败不会影响系统的继续运作)

zookeeper: 从 Zookeeper 的实际应用情况来看, 在使用 Zookeeper 获取服务列表时,
如果此时的 Zookeeper 集群中的 Leader 宕机了, 该集群就要进行 Leader 的选举,
又或者 Zookeeper 集群中半数以上服务器节点不可用, 那么将无法处理该请求. 所以说, Zookeeper 不能保证服务可用性

eureka: Eureka Server 采用的是Peer to Peer 对等通信, 这是一种去中心化的架构,
无 master/slave 之分，每一个 Peer 都是对等的, 在这种架构风格中, 节点通过彼此互相注册来提高可用性,
每个节点需要添加一个或多个有效的 serviceUrl 指向其他节点. 每个节点都可被视为其他节点的副本
在集群环境中如果某台 Eureka Server 宕机, Eureka Client 的请求会自动切换到新的 Eureka Server 节点上,
当宕机的服务器重新恢复后, Eureka 会再次将其纳入到服务器集群管理之中. 当节点开始接受客户端请求时,
所有的操作都会在节点间进行复制（replicate To Peer）操作, 将请求复制到该 Eureka Server 当前所知的其它所有节点中

consul: Consul 遵循CAP原理中的CP原则, 保证了强一致性和分区容错性, 且使用的是Raft算法,
比zookeeper使用的Paxos算法更加简单. 虽然保证了强一致性, 但是可用性就相应下降了,
例如服务注册的时间会稍长一些, 因为 Consul 的 raft 协议要求必须过半数的节点都写入成功才认为注册成功,
在leader挂掉了之后, 重新选举出leader之前会导致Consul 服务不可用

nacos: 基于AP

#####################################

zookeeper分布式锁:
获取锁:
首先, 在Zookeeper当中创建一个持久节点ParentLock. 当第一个客户端想要获得锁时,
需要在ParentLock这个节点下面创建一个临时顺序节点 Lock1.
之后, Client1查找ParentLock下面所有的临时顺序节点并排序, 判断自己所创建的节点Lock1是不是顺序最靠前的一个,
如果是第一个节点, 则成功获得锁, 这时候, 如果再有一个客户端 Client2 前来获取锁,
则在ParentLock下载再创建一个临时顺序节点Lock2. Client2查找ParentLock下面所有的临时顺序节点并排序,
判断自己所创建的节点Lock2是不是顺序最靠前的一个, 结果发现节点Lock2并不是最小的.

于是, Client2向排序仅比它靠前的节点Lock1注册Watcher, 用于监听Lock1节点是否存在,
这意味着Client2抢锁失败, 进入了等待状态. 这时候, 如果又有一个客户端Client3前来获取锁,
则在ParentLock下载再创建一个临时顺序节点Lock3. Client3查找ParentLock下面所有的临时顺序节点并排序,
判断自己所创建的节点Lock3是不是顺序最靠前的一个, 结果同样发现节点Lock3并不是最小的.

于是, Client3向排序仅比它靠前的节点Lock2注册Watcher, 用于监听Lock2节点是否存在.
这意味着Client3同样抢锁失败, 进入了等待状态. 这样一来, Client1得到了锁, Client2监听了Lock1,
Client3监听了Lock2. 这恰恰形成了一个等待队列,
很像是Java当中ReentrantLock所依赖的AQS(AbstractQueuedSynchronizer).

释放锁:
释放锁分为两种情况:
1.任务完成，客户端显示释放
当任务完成时, Client1会显示调用删除节点Lock1的指令.

2.任务执行过程中，客户端崩溃
获得锁的Client1在任务执行过程中, 如果Duang的一声崩溃, 则会断开与Zookeeper服务端的链接.
根据临时节点的特性, 相关联的节点Lock1会随之自动删除.
由于Client2一直监听着Lock1的存在状态, 当Lock1节点被删除, Client2会立刻收到通知.
这时候Client2会再次查询ParentLock下面的所有节点, 确认自己创建的节点Lock2是不是目前最小的节点.
如果是最小, 则Client2顺理成章获得了锁. 同理, 如果Client2也因为任务完成或者节点崩溃而删除了节点Lock2,
那么Cient3就会接到通知. 最终, Client3成功得到了锁.

#####################################

zk watcher 事件机制原理
1. 客户端注册Watcher到服务端;
2. 服务端发生数据变更;
3. 服务端通知客户端数据变更;
4. 客户端回调Watcher处理变更应对逻辑;

注册 watcher 有 3 种方式，getData、exists、getChildren.
ZK的所有读操作都可以设置watch监视点: getData, getChildren, exists. 写操作则是不能设置监视点的.
监视有两种类型:
数据监视点和子节点监视点.
创建、删除或者设置znode都会触发这些监视点.
exists,getData 可以设置数据监视点.
getChildren 可以设置子节点变化。

监测的事件类型有:
None // 客户端连接状态发生变化的时候 会收到None事件
NodeCreated // 节点创建事件
NodeDeleted // 节点删除事件
NodeDataChanged // 节点数据变化
NodeChildrenChanged // 子节点被创建 删除触发该事件
