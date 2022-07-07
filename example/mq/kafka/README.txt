修改配置:
server.properties:
listeners=PLAINTEXT://192.168.221.128:9092
log.dirs=./data
num.partitions=12
zookeeper.connect=localhost:2181

启动命令:
./bin/kafka-server-start.sh -daemon ./config/server.properties

#####################################

kafka相关概念:

Producer:
producer即生产者, 消息的产生者, 是消息的入口

Broker:
broker是kafka实例, 每个服务器上有一个或多个kafka的实例, 我们姑且认为每个broker对应一台服务器.
每个kafka集群内的broker都有一个不重复的编号, 如图中的broker-0、broker-1等

Topic:
消息的主题, 可以理解为消息的分类, kafka的数据就保存在topic. 在每个broker上都可以创建多个topic

Partition:
topic的分区, 每个topic可以有多个分区, 分区的作用是做负载, 提高kafka的吞吐量.
同一个topic在不同的分区的数据是不重复的, partition的表现形式就是一个一个的文件夹

Replication:
每一个分区都有多个副本, 副本的作用是做备胎. 当主分区（Leader）故障的时候会选择一个备胎（Follower）上位,
成为Leader. 在kafka中默认副本的最大数量是10个, 且副本的数量不能大于Broker的数量,
follower和leader绝对是在不同的机器, 同一机器对同一个分区也只可能存放一个副本（包括自己）

Message:
每一条发送的消息主体

Consumer:
消费者, 即消息的消费方, 是消息的出口

Consumer Group:
我们可以将多个消费组组成一个消费者组, 在kafka的设计中同一个分区的数据只能被消费者组中的某一个消费者消费.
同一个消费者组的消费者可以消费同一个topic的不同分区的数据, 这也是为了提高kafka的吞吐量

Zookeeper:
kafka集群依赖zookeeper来保存集群的的元信息, 来保证系统的可用性

#####################################

1. kafka数据目录
log.dirs: 提升读写性能、故障转移, 一块磁盘挂了转移到另一个上

#####################################

kafka零拷贝
零拷贝并不是不需要拷贝, 而是减少不必要的拷贝次数, 通常是说在IO读写过程中
传统IO流程: 先读取、再发送, 实际经过1~4四次copy
1、第一次: 将磁盘文件, 读取到操作系统内核缓冲区
2、第二次: 将内核缓冲区的数据, copy到application应用程序的buffer
3、第三步: 将application应用程序buffer中的数据, copy到socket网络发送缓冲区(属于操作系统内核的缓冲区)
4、第四次: 将socket buffer的数据, copy到网卡, 由网卡进行网络传输

kafka利用数据可以直接从读缓冲区传输到套接字缓冲区, 实现性能

kafka用了磁盘, 还速度快:
1、顺序读写
磁盘顺序读或写的速度400M/s, 能够发挥磁盘最大的速度
随机读写, 磁盘速度慢的时候十几到几百K/s

2、零拷贝 sendfile(in,out)
数据直接在内核完成输入和输出, 不需要拷贝到用户空间再写出去
kafka数据写入磁盘前, 数据先写到进程的内存空间

3、Memory Mapped Files, 简称mmap文件映射
简单描述其作用就是: 将磁盘文件映射到内存, 用户通过修改内存就能修改磁盘文件
在进程的非堆内存开辟一块内存空间, 和OS内核空间的一块内存进行映射, kafka数据写入、是写入这块内存空间,
但实际这块内存和OS内核内存有映射, 也就是相当于写在内核内存空间了, 且这块内核空间、内核直接能够访问到, 直接落入磁盘

mmap也有一个很明显的缺陷——不可靠, 写到mmap中的数据并没有被真正的写到硬盘,
操作系统会在程序主动调用flush的时候才把数据真正的写到硬盘.
Kafka提供了一个参数——producer.type来控制是不是主动flush,
如果Kafka写入到mmap之后就立即flush然后再返回Producer叫同步(sync),
写入mmap之后立即返回Producer不调用flush叫异步(async)

磁盘文件通过网络发送
Linux 2.4+ 内核通过 sendfile 系统调用, 提供了零拷贝. 磁盘数据通过 DMA 拷贝到内核态 Buffer 后,
直接通过 DMA 拷贝到 NIC Buffer(socket buffer)，无需 CPU 拷贝, 这也是零拷贝这一说法的来源.
除了减少数据拷贝外, 因为整个读文件 - 网络发送由一个 sendfile 调用完成,
整个过程只有两次上下文切换, 因此大大提高了性能


Kafka快的原因:
1、partition顺序读写, 充分利用磁盘特性, 这是基础
2、Producer生产的数据持久化到broker, 采用mmap文件映射, 实现顺序的快速写入
3、Customer从broker读取数据, 采用sendfile, 将磁盘文件读到OS内核缓冲区后, 直接转到socket buffer进行网络发送

mmap 和 sendfile总结:
1、都是Linux内核提供、实现零拷贝的API
2、sendfile 是将读到内核空间的数据, 转到socket buffer, 进行网络发送
3、mmap将磁盘文件映射到内存, 支持读和写, 对内存的操作会反映在磁盘文件上
RocketMQ 在消费消息时, 使用了 mmap, kafka 使用了 sendFile

#####################################

一个分区不会让消费者组里面的多个消费者去消费,
但是在消费者不饱和的情况下, 一个消费者是可以去消费多个分区的数据的
消费者组的consumer的数量与partition的数量一致

#####################################

producer怎么知道该将数据发往哪个partition:

1. partition在写入的时候可以指定需要写入的partition, 如果有指定, 则写入对应的partition
2. 如果没有指定partition, 但是设置了数据的key, 则会根据key的值hash出一个partition
3. 如果既没指定partition, 又没有设置key, 则会轮询选出一个partition

Partition结构:
partition在服务器上的表现形式就是一个一个的文件夹, 每个partition的文件夹下面会有多组segment文件,
每组segment文件又包含.index文件、.log文件、.timeindex文件（早期版本中没有）三个文件,
log文件就实际是存储message的地方, 而index和timeindex文件为索引文件, 用于检索消息

#####################################

Message结构:
1. offset是一个占8byte的有序id号, 它可以唯一确定每条消息在parition内的位置
2. 消息大小: 消息大小占用4byte, 用于描述消息的大小
3. 消息体: 消息体存放的是实际的消息数据（被压缩过）, 占用的空间根据具体的消息而不一样

#####################################

存储策略
无论消息是否被消费, kafka都会保存所有的消息. 旧数据删除策略:

1. 基于时间, 默认配置是168小时（7天）
2. 基于大小, 默认配置是1073741824

需要注意的是, kafka读取特定消息的时间复杂度是O(1), 所以这里删除过期的文件并不会提高kafka的性能

#####################################

消费数据
要查找一个offset为368801的message的过程:
1. 先找到offset的368801message所在的segment文件（利用二分法查找）, 这里找到的就是在第二个segment文件

2. 打开找到的segment中的.index文件
（也就是368796.index文件, 该文件起始偏移量为368796+1,
我们要查找的offset为368801的message在该index内的偏移量为368796+5=368801,
所以这里要查找的相对offset为5）. 由于该文件采用的是稀疏索引的方式存储着相对offset及对应message物理偏移量的关系,
所以直接找相对offset为5的索引找不到,
这里同样利用二分法查找相对offset小于或者等于指定的相对offset的索引条目中最大的那个相对offset,
所以找到的是相对offset为4的这个索引

3. 根据找到的相对offset为4的索引确定message存储的物理偏移位置为256.
打开数据文件, 从位置为256的那个地方开始顺序扫描直到找到offset为368801的那条Message

这套机制是建立在offset为有序的基础上,
利用segment+有序offset+稀疏索引+二分查找+顺序查找等多种手段来高效的查找数据

#####################################
kafka保证消息不丢失

(1). ACK机制保证消息不丢失
生产者、kafka内部、消费者三处ack确保消息不丢失
ACK有三种
0: 意味着producer不等待broker同步完成的确认,
继续发送下一条(批)信息提供了最低的延迟, 但是最弱的持久性,
当服务器发生故障时, 就很可能发生数据丢失

1: 意味着producer要等待leader成功收到数据并得到确认,
才发送下一条message, 此选项提供了较好的持久性较低的延迟性
Partition的Leader死亡，follwer尚未复制，数据就会丢失

-1: 意味着producer得到follwer确认, 才发送下一条数据

(2). 在获取kafka的消息后正准备入库（未入库）, 但是消费者挂了, 那么如果让kafka自动去维护offset,
它就会认为这条数据已经被消费了, 那么会造成数据丢失
解决: 使用kafka高级API, 自己手动维护偏移量, 当数据入库之后进行偏移量的更新（适用于基本数据源）

#####################################

调整单机的消费能力大约可以从下面这几个方面考虑:
1.剥离消息获取和消息处理逻辑，即 使用额外的线程进行消息处理
2.根据CPU使用率考虑是否使用多个额外线程并行处理消息处理
3.简化消息处理逻辑
4.加快消息获取速度, 适当提高fetch.min.bytes,
如果consumer和broker之间RTT很大, 适当增加socket buffer等

#####################################

kafka顺序消费:
出现顺序乱的原因或背景:
a、同一个订单队列, 被多个消费者消费, 就会导致顺序错乱
b、同一个订单号的不同行为, 被分配到不同的队列里, 被不同的消费者消费, 也会造成错乱

解决思想:
a、相同行为的消息存放到同一个MQ服务器中, 生产者在发送消息的时候指定要发送到哪个Partition(分区)
b、最终只会有单个消费者去消费

#####################################

kafka分区的状态:
NewPartition: 分区被创建后被设置成这个状态, 表明它是一个全新的分区对象.
处于这个状态的分区, 被 Kafka 认为是“未初始化”, 因此, 不能选举 Leader.

OnlinePartition: 分区正式提供服务时所处的状态.

OfflinePartition: 分区下线后所处的状态.

NonExistentPartition: 分区被删除, 并且从分区状态机移除后所处的状态.

#####################################

分区 Leader 选举有 4 种场景：
OfflinePartitionLeaderElectionStrategy: 因为 Leader 副本下线而引发的分区 Leader 选举.
ReassignPartitionLeaderElectionStrategy: 因为执行分区副本重分配操作而引发的分区 Leader 选举.
PreferredReplicaPartitionLeaderElectionStrategy: 因为执行 Preferred 副本 Leader 选举而引发的分区 Leader 选举.
ControlledShutdownPartitionLeaderElectionStrategy: 因为正常关闭 Broker 而引发的分区 Leader 选举.

#####################################

kafka Consumer的Reblance机制介绍:
Reblance本质上是一种协议, 规定了一个Consumer Group下的所有的Consumer如何达成一致来分配订阅Topic的每个Partition.
比如某个group下有5个consumer, 它订阅了一个具有10个分区的topic. 正常情况下,
Kafka平均会为每个consumer分配2个分区, 这个分配的过程就叫rebalance.

Rebalance的触发条件:
1.有新的消费者加入Consumer Group
2.有消费者下线, 可能由于长时间未向GroupCoordinator(协调者)发送心跳, GroupCoordinator会认为其已下线.
3.有消费者主动退出Consumer Group
4.订阅的topic分区出现变化
5.调用unsubscribe()取消对某Topic的订阅
即Consumer或者Topic自身发生变化时, 会触发Rebalance.
