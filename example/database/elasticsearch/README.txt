安装:
docker run -itd --name elasticsearch -e ES_JAVA_OPTS="-Xms512m -Xmx512m" -e "discovery.type=single-node" -p 9200:9200 -p 9300:9300 elasticsearch:7.1.1

#####################################

#节点1的配置信息
#集群名称, 保证唯一
cluster.name: my-elasticsearch
#节点名称, 必须不一样
node.name: node-01
# 是否为master
node.master = true
# 是否存数据
node.data = false
#必须为本机的ip地址
network.host: 127.0.0.1
#服务端口号, 在同一机器下必须不一样
http.port: 9200
#集群间通信端口号, 在同一机器下必须不一样
transport.tcp.port: 9300
# 最小master数量
discovery.zen.minimum_master_nodes = 1
#设置集群自动发现机器候选主节点集合
discovery.zen.ping.unicast.hosts: ["127.0.0.1:9300","127.0.0.1:9301","127.0.0.1:9302"]

#####################################

ES集群中的节点有三种不同的类型:

主节点: 负责管理集群范围内的所有变更, 例如增加、删除索引, 或者增加、删除节点等, 主节点并不需要涉及到文档级别的变更和搜索等操作, 可以通过属性node.master进行设置
数据节点: 存储数据和其对应的倒排索引, 默认每一个节点都是数据节点（包括主节点）, 可以通过node.data属性进行设置
协调节点: 如果node.master和node.data属性均为false, 则此节点称为协调节点, 用来响应客户请求, 均衡每个节点的负载

当node.master为true时, 其表示这个node是一个master的候选节点, 可以参与选举, 在ES的文档中常被称作master-eligible node, 类似于MasterCandidate. ES正常运行时只能有一个master(即leader), 多于1个时会发生脑裂
当node.data为true时, 这个节点作为一个数据节点, 会存储分配在该node上的shard的数据并负责这些shard的写入、查询等
此外, 任何一个集群内的node都可以执行任何请求, 其会负责将请求转发给对应的node进行处理, 所以当node.master和node.data都为false时, 这个节点可以作为一个类似proxy的节点, 接受请求并进行转发、结果聚合等

ZenDiscovery是ES自己实现的一套用于节点发现和选主等功能的模块, 没有依赖Zookeeper等工具

#####################################

Master选举:
集群中可能会有多个master-eligible node, 此时就要进行master选举, 保证只有一个当选master
如果有多个node当选为master, 则集群会出现脑裂, 脑裂会破坏数据的一致性, 导致集群行为不可控, 产生各种非预期的影响

为了避免产生脑裂, ES采用了常见的分布式系统思路, 保证选举出的master被多数派(quorum)的master-eligible node认可,
以此来保证只有一个master, 这个quorum通过以下配置进行配置:
conf/elasticsearch.yml:
    discovery.zen.minimum_master_nodes: 2
这个配置对于整个集群非常重要

#####################################

master选举谁发起, 什么时候发起:
master选举当然是由master-eligible节点发起, 当一个master-eligible节点发现满足以下条件时发起选举:
该master-eligible节点的当前状态不是master
该master-eligible节点通过ZenDiscovery模块的ping操作询问其已知的集群其他节点, 没有任何节点连接到master
包括本节点在内, 当前已有超过minimum_master_nodes个节点没有连接到master
总结一句话, 即当一个节点发现包括自己在内的多数派的master-eligible节点认为集群没有master时, 就可以发起master选举

#####################################

当需要选举master时, 选举谁:
选举的是排序后的第一个MasterCandidate
先根据节点的clusterStateVersion比较, clusterStateVersion越大, 优先级越高
clusterStateVersion相同时, 进入compareNodes, 其内部按照节点的Id比较(Id为节点第一次启动时随机生成)

#####################################

什么时候选举成功:
假设Node_A选Node_B当Master:
Node_A会向Node_B发送join请求, 那么此时:
(1) 如果Node_B已经成为Master, Node_B就会把Node_A加入到集群中, 然后发布最新的cluster_state,
最新的cluster_state就会包含Node_A的信息, 相当于一次正常情况的新节点加入, 对于Node_A,
等新的cluster_state发布到Node_A的时候, Node_A也就完成join了

(2) 如果Node_B在竞选Master, 那么Node_B会把这次join当作一张选票, 对于这种情况, Node_A会等待一段时间,
看Node_B是否能成为真正的Master, 直到超时或者有别的Master选成功

(3) 如果Node_B认为自己不是Master(现在不是，将来也选不上), 那么Node_B会拒绝这次join,
对于这种情况, Node_A会开启下一轮选举

假设Node_A选自己当Master:
此时NodeA会等别的node来join, 即等待别的node的选票, 当收集到超过半数的选票时, 认为自己成为master,
然后变更cluster_state中的master node为自己, 并向集群发布这一消息

#####################################

优化点:
在es中, 当索引一个文档时, 当全部副本都索引数据后, es才会返回给客户端,
因此, 如果为了提高索引速度, 我们可以在索引文档之前, 减少各个分片的副本数目,
在索引文档之后, 修改索引分片的副本数据, 扩增副本数, 进行查询的负载均衡, 提高查询速度
在es内, 进行数据检索的时候, 节点将检索请求分发到各数据节点分别进行检索后,
再汇集起来, 传递给客户端, 为了提高检索的速度, 我们可以在集群内增加协调者的数量,
协调者只用于建立和客户端之间的连接, 并不进行数据的检索, 这样就可以让数据节点专心进行数据的检索, 不用考虑和客户端间的连接
将路由和别名相互结合会很大的提升索引, 查询效率。

#####################################

分布式文档CRUD
1. 索引新文档（Create）
当用户向一个节点提交了一个索引新文档的请求, 节点会计算新文档应该加入到哪个分片（shard）中
每个节点都存储有每个分片存储在哪个节点的信息, 因此协调节点会将请求发送给对应的节点, 注意这个请求会发送给主分片,
等主分片完成索引, 会并行将请求发送到其所有副本分片, 保证每个分片都持有最新数据

每次写入新文档时, 都会先写入内存中, 并将这一操作写入一个translog文件（transaction log）中,
此时如果执行搜索操作, 这个新文档还不能被索引到

# 将新添加的内存数据刷新到文件系统缓存中
# index.refresh_interval: 120s
ES会每隔1秒时间（这个时间可以修改）进行一次刷新操作（refresh）,
此时在这1秒时间内写入内存的新文档都会被写入一个文件系统缓存（filesystem cache）中, 并构成一个分段（segment）
此时这个segment里的文档可以被搜索到, 但是尚未写入硬盘, 即如果此时发生断电, 则这些文档可能会丢失
在执行刷新后清空内存, 新文档写入文件系统缓存
不断有新的文档写入, 则这一过程将不断重复执行, 每隔一秒将生成一个新的segment, 而translog文件将越来越大

每隔30分钟或者translog文件变得很大, 则执行一次fsync操作
此时所有在文件系统缓存中的segment将被写入磁盘, 而translog将被删除（此后会生成新的translog）
执行fsync后segment写入磁盘, 清空内存和translog

由上面的流程可以看出, 在两次fsync操作之间, 存储在内存和文件系统缓存中的文档是不安全的,
一旦出现断电这些文档就会丢失, 所以ES引入了translog来记录两次fsync之间所有的操作,
这样机器从故障中恢复或者重新启动, ES便可以根据translog进行还原

# translog flush 间隔调整
# index.translog.sync_interval: 120s
# index.translog.flush_threshold_size: 1024mb
# index.translog.flush_threshold_period: 120m
当然, translog本身也是文件, 存在于内存当中, 如果发生断电一样会丢失
因此, ES会在每隔5秒时间或是一次写入请求完成后将translog写入磁盘,
可以认为一个对文档的操作一旦写入磁盘便是安全的可以复原的, 因此只有在当前操作记录被写入磁盘,
ES才会将操作成功的结果返回发送此操作请求的客户端

# segment merge参数调整
# index.merge.policy.floor_segment属性用于阻止segment 的频繁flush, 小于此值将考虑优先合并, 默认为2M
此外, 由于每一秒就会生成一个新的segment, 很快将会有大量的segment,
对于一个分片进行查询请求, 将会轮流查询分片中的所有segment, 这将降低搜索的效率,
因此ES会自动启动合并segment的工作, 将一部分相似大小的segment合并成一个新的大segment,
合并的过程实际上是创建了一个新的segment, 当新segment被写入磁盘, 所有被合并的旧segment被清除


2. 读操作（Read）: 查询过程
查询的过程大体上分为查询（query）和取回（fetch）两个阶段,
这个节点的任务是广播查询请求到所有相关分片, 并将它们的响应整合成全局排序后的结果集合, 这个结果集合会返回给客户端

a. 查询阶段
当一个节点接收到一个搜索请求, 则这个节点就变成了协调节点
查询过程分布式搜索
第一步是广播请求到索引中每一个节点的分片拷贝, 查询请求可以被某个主分片或某个副本分片处理,
协调节点将在之后的请求中轮询所有的分片拷贝来分摊负载

每个分片将会在本地构建一个优先级队列, 如果客户端要求返回结果排序中从第from名开始的数量为size的结果集,
则每个节点都需要生成一个from+size大小的结果集, 因此优先级队列的大小也是from+size,
分片仅会返回一个轻量级的结果给协调节点, 包含结果集中的每一个文档的ID和进行排序所需要的信息

协调节点会将所有分片的结果汇总，并进行全局排序，得到最终的查询排序结果。此时查询阶段结束。

b. 取回阶段
查询过程得到的是一个排序结果, 标记出哪些文档是符合搜索要求的, 此时仍然需要获取这些文档返回客户端

协调节点会确定实际需要返回的文档, 并向含有该文档的分片发送get请求,
分片获取文档返回给协调节点, 协调节点将结果返回给客户端

#####################################

Elasticsearch更新和删除文档的过程:
删除和更新也都是写操作, 但是Elasticsearch中的文档是不可变的, 因此不能被删除或者改动以展示其变更
磁盘上的每个段都有一个相应的.del文件, 当删除请求发送后, 文档并没有真的被删除, 而是在.del文件中被标记为删除
该文档依然能匹配查询, 但是会在结果中被过滤掉. 当段合并时, 在.del文件中被标记为删除的文档将不会被写入新段
在新的文档被创建时, Elasticsearch会为该文档指定一个版本号, 当执行更新时, 旧版本的文档在.del文件中被标记为删除,
新版本的文档被索引到一个新段, 旧版本的文档依然能匹配查询, 但是会在结果中被过滤掉

#####################################

索引过程调整和优化
a. 自动生成docID
b. 调整字段 Mappings, 字段的index属性设置为: not_analyzed, 或者no
c. 单个doc在建立索引时的运算复杂度,最大的因素不在于doc的字节数或者说某个字段value的长度, 而是字段的数量
d. 使用不同的分析器: analyzer
e. 禁用_all字段

#####################################

es扩容、缩容、升级方案
缩容: 在es中提供了一种停用节点的机制, 在设置节点停用后, es将不会在给该节点分配新的分片,
同时, 该节点上的分片将会被转移到其他的节点之上, 停用节点后, 我们通过 _nodes端点来确定节点的id值
curl 'localhost:9200/_nodes?pretty'
然后我们使用获取集群的状态, 通过集群状态查看节点上是否存在分片
curl 'localhost:9200/_cluster/state/routing_table,routing_nodes?pretty'

升级: 轮流重启的含义是依次对es中的各个节点进行升级操作, 但是在轮流重启之前, 需要注意一个问题,
在es中, 如果节点下线, 那么会出发es的索引重新分配操作, 该操作会将副本索引分片升级为主索引分片,
同时, 新建副本索引分片, 其实并不需要索引分片重建的过程, 数据依然存在节点, 只需要节点es版本升级后,
数据就进行恢复了, 所以在进行轮流重启之前, 我们首先需要关闭自动分配, 关闭自动分配后,
es只会将副本分配升级为主分片, 但是并不会进行副本分配重建的过程
curl -XPUT 'localhost:9200/_cluster/settings' -d '{ "transient":{ "cluster.routing.allocation.enable":"all" } }'
滚动升级的正确流程是:
1. 关闭自动分配
2. 升级单个节点
3. 开启自动分配, 等待集群变为绿色
4. 重复以上步骤, 去升级别的节点
3. _catAPI cat com.api 提供了集群的健康、分片、索引等信息, 便于用户对集群进行调试, - 通过_cat com.api, 可以获取该api支持的操作类型


#####################################
