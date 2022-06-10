helm工作原理:
1. Chart install
helm从制定目录或tar文件解析chart结构信息
helm将制定的chart结构和value信息通过gRPC协议传递给tiller
tiller根据chart和values生成一个release
tiller通过json将release发送给kubernetes, 生成release
案例: helm install --name elasticsearch -f values.yaml stable/elasticsearch

2. Chart update
helm从制定的目录或tar文件解析chart结构信息
helm将制定的chart结构和value信息通过gRPC协议传给tiller
tiller生成release并更新制定名称的release的history
tiller将release信息发送给kubernetes用于更新release
案例: helm upgrade go2cloud-com.api-doc go2cloud-com.api-doc/

3. Chart Rollback
helm将会滚的release名称传递给tiller
tiller根据release名称查找history
tiller从history中获取到上一个release
tiller将上一个release发送给kubernetes用于替换当前release
案例: helm rollback db-mysql 1


4. Chart delete
案例: helm delete --purge db-mysql

#####################################

k8s核心组件

coredns: 识别service
dns:
kube-apiserver: 提供了资源操作的唯一入口, 并提供认证、授权、访问控制、API 注册和发现等机制
kube-controller: 负责维护集群的状态, 比如故障检测、自动扩展、滚动更新等
kube-flannel: 网络组件
kube-proxy:     负责为Service提供cluster内部的服务发现和负载均衡
kube-scheduler: 负责资源的调度, 按照预定的调度策略将Pod调度到相应的机器上
nvidia-device-plugin: nvidia-gpu插件
etcd: 保存了整个集群的状态

kubernetes-dashboard: pod管理界面