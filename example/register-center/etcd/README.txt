安装
docker run -itd -p 2379:2379 --restart=always --name etcd quay.io/coreos/etcd /usr/local/bin/etcd --advertise-client-urls http://0.0.0.0:2379 --listen-client-urls http://0.0.0.0:2379

#####################################

etcd 是一个高度一致的分布式键值存储
采用 raft 算法, 实现分布式系统数据的可用性和一致性

etcd的存储有如下特点:
采用kv型数据存储, 一般情况下比关系型数据库快
支持动态存储(内存)以及静态存储(磁盘)
分布式存储, 可集成为多节点集群
存储方式, 采用类似目录结构
只有叶子节点才能真正存储数据, 相当于文件
叶子节点的父节点一定是目录, 目录不能存储数据
