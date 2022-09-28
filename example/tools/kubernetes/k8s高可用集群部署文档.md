# k8s高可用集群部署文档





[TOC]





## 1、安装要求



部署kubernetes集群需要满足以下几个条件：

- 机器数量：三台机器+
- 操作系统：CentOS7.5+
- 硬件配置：2GBRAM+，2vCPU+，硬盘30GB+
- 网络要求：集群中所有机器之间网络互通



## 2、安装步骤

集群规划

| 角色    | IP             |
| ------- | -------------- |
| master1 | 192.168.10.201 |
| master2 | 192.168.10.202 |
| master3 | 192.168.10.203 |
| node1   | 192.168.10.204 |
| node2   | 192.168.10.205 |



### 2.1、安装前预处理操作



#### （1）配置主机名

master1节点设置：

```go
hostnamectl set-hostname master1
```



master2节点设置：

```go
hostnamectl set-hostname master2
```



master3节点设置：

```go
hostnamectl set-hostname master3
```



node1从节点设置：

```go
hostnamectl set-hostname node1
```



node2从节点设置：

```go
hostnamectl set-hostname node2
```



#### （2）添加hosts

所有的节点都要添加hosts解析记录，由于coredns可能无限重启，因此网卡配置DNS1信息

```go
cat >>/etc/hosts <<EOF
192.168.10.201 master1
192.168.10.202 master2
192.168.10.203 master3
192.168.10.204 node1
192.168.10.205 node2
EOF
```



#### （3）配置免密

在master1节点生成密钥对，一路回车，并分发给其他的所有主机

```go
ssh-keygen

ssh-copy-id -i ~/.ssh/id_rsa.pub  root@192.168.10.201
ssh-copy-id -i ~/.ssh/id_rsa.pub  root@192.168.10.202
ssh-copy-id -i ~/.ssh/id_rsa.pub  root@192.168.10.203
ssh-copy-id -i ~/.ssh/id_rsa.pub  root@192.168.10.204
ssh-copy-id -i ~/.ssh/id_rsa.pub  root@192.168.10.205
```



#### （4）升级内核（可以不处理）

通过下载kernel image的rpm包进行安装

centos7系统：http://elrepo.org/linux/kernel/el7/x86_64/RPMS/

```
kernel-lt-5.4.195-1.el7.elrepo.x86_64.rpm
kernel-lt-devel-5.4.195-1.el7.elrepo.x86_64.rpm
```

编写shell脚本升级内核

```go
#!/bin/bash
# ----------------------------
# upgrade kernel by bomingit@126.com
# ----------------------------

yum localinstall -y kernel-lt*
if [ $? -eq 0 ];then
 grub2-set-default 0 && grub2-mkconfig -o /etc/grub2.cfg
 grubby --args="user_namespace.enable=1" --update-kernel="$(grubby --default-kernel)"
fi
echo "please reboot your system quick!!!"
```



注意：一定要重启机器

验证内核版本

```
uname -r
```



#### （5）关闭防火墙selinux

```go
systemctl disable --now firewalld
setenforce 0
sed -i 's/enforcing/disabled/' /etc/selinux/config 
```

上面的是临时关闭，当然也可以永久关闭，即在/etc/fstab文件中将swap挂载所在的行注释掉即可



#### （6）关闭swap分区

```go
swapoff -a
sed -i.bak 's/^.*centos-swap/#&/g' /etc/fstab
```

第一条是临时关闭，当然也可以使用第二条永久关闭，后者手动在/etc/fstab文件中将swap挂载所在的行注释掉即可



#### （7）优化内核

```go
cat > /etc/sysctl.d/k8s.conf << EOF
net.ipv4.ip_forward = 1
net.bridge.bridge-nf-call-iptables = 1
net.bridge.bridge-nf-call-ip6tables = 1
fs.may_detach_mounts = 1
vm.overcommit_memory=1
vm.panic_on_oom=0
fs.inotify.max_user_watches=89100
fs.file-max=52706963
fs.nr_open=52706963
net.ipv4.tcp_keepalive_time = 600
net.ipv4.tcp.keepaliv.probes = 3
net.ipv4.tcp_keepalive_intvl = 15
net.ipv4.tcp.max_tw_buckets = 36000
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp.max_orphans = 327680
net.ipv4.tcp_orphan_retries = 3
net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_max_syn_backlog = 16384
net.ipv4.ip_conntrack_max = 65536
net.ipv4.tcp_max_syn_backlog = 16384
net.ipv4.top_timestamps = 0
net.core.somaxconn = 16384
EOF
```

使其立即生效

```go
sysctl --system
```



#### （8）配置yum源

配置本地yum源，修改rpms包路径

```
mv /etc/yum.repos.d/* /tmp/

cat > /etc/yum.repos.d/local.repo << EOF
[CentOS7]
name=CentOS-7.5
baseurl=file:///home/rpms
enabled=1
gpgcheck=0
EOF

yum clean all
```





#### （9）时区与时间同步

```go
yum install epel-release -y
yum install ntpdate -y
ntpdate 192.168.10.201
```



### 2.2、安装docker

#### （1）安装docker-ce组件

所有的节点都需要安装docker服务

```go
yum install -y  docker-ce docker-ce-cli
```



#### （2）启动docker并设置开机自启动

```go
systemctl enable --now docker
```

查看版本号，检测docker是否安装成功

```go
docker --version
```



#### （3）更换docker的镜像仓库源

默认的镜像仓库地址是docker官方的，国内访问异常缓慢，因此更换为个人的镜像源

```go
cat > /etc/docker/daemon.json << EOF
{
  "registry-mirrors": ["https://my.mirror.com"],
  "graph": "/home/k8s",
  "default-shm-size": "64g"
}
EOF
```

由于重新加载docker仓库源，所以需要重启docker

```go
systemctl restart docker
```



### 2.3、安装kubernetes

#### （1）安装kubeadm、kubelet和kubectl组件

所有的节点都需要安装这几个组件

```go
yum install -y kubelet-1.20.0 kubeadm-1.20.0 kubectl-1.20.0
```



#### （2）设置开机自启动

我们先设置开机自启，但是kubelet服务暂时先不启动

```go
systemctl enable kubelet
```



### 2.4、haproxy+keepalived配置高可用vip

高可用我们采用官方推荐的haproxy+keepalived，haproxy和keepalived以守护进程的方式在所有master节点部署



#### （1）安装keepalived和haproxy

注意：只需要在三个master节点安装即可

```go
yum install -y keepalived haproxy 
```



#### （2）配置haproxy服务

所有master节点的haproxy配置相同，haproxy的配置文件是/etc/haproxy/haproxy.cfg，master1节点配置完成之后再分发给master2、master3两个节点

```go
cat > /etc/haproxy/haproxy.cfg << EOF
global
  maxconn  2000
  ulimit-n  16384
  log  127.0.0.1 local0 err
  stats timeout 30s

defaults
  log global
  mode  http
  option  httplog
  timeout connect 5000
  timeout client  50000
  timeout server  50000
  timeout http-request 15s
  timeout http-keep-alive 15s

frontend monitor-in
  bind *:33305
  mode http
  option httplog
  monitor-uri /monitor

listen stats
  bind    *:8006
  mode    http
  stats   enable
  stats   hide-version
  stats   uri       /stats
  stats   refresh   30s
  stats   realm     Haproxy\ Statistics
  stats   auth      admin:admin

frontend k8s-master
  bind 0.0.0.0:8443
  bind 127.0.0.1:8443
  mode tcp
  option tcplog
  tcp-request inspect-delay 5s
  default_backend k8s-master

backend k8s-master
  mode tcp
  option tcplog
  option tcp-check
  balance roundrobin
  default-server inter 10s downinter 5s rise 2 fall 2 slowstart 60s maxconn 250 maxqueue 256 weight 100
  server master1 192.168.10.201:6443  check inter 2000 fall 2 rise 2 weight 100
  server master2 192.168.10.202:6443  check inter 2000 fall 2 rise 2 weight 100
  server master3 192.168.10.203:6443  check inter 2000 fall 2 rise 2 weight 100
EOF
```

注意这里的三个master节点的ip地址要根据你自己的情况配置好



#### （3）配置keepalived服务

keepalived中使用track_script机制来配置脚本进行探测kubernetes的master节点是否宕机，并以此切换节点实现高可用

master1节点的keepalived配置文件如下所示，配置文件所在的位置/etc/keepalived/keepalived.conf，修改vip地址

```go
cat > /etc/keepalived/keepalived.conf << EOF
! Configuration File for keepalived
global_defs {
    router_id LVS_DEVEL
}
vrrp_script chk_kubernetes {
    script "/etc/keepalived/check_kubernetes.sh"  # 检查脚本
    interval 2
    weight -5
    fall 3  
    rise 2
}
vrrp_instance VI_1 {
    state MASTER
    interface ens33        # 网卡名
    virtual_router_id 51
    priority 100
    advert_int 2
    authentication {
        auth_type PASS
        auth_pass K8SHA_KA_AUTH
    }
    virtual_ipaddress {
        192.168.10.33       # vip
    }
#    track_script {
#       chk_kubernetes
#    }
}
EOF
```



#### （4）配置健康检测脚本

我这里将健康检测脚本放置在/etc/keepalived目录下，check_kubernetes.sh检测脚本如下：

```go
cat > /etc/keepalived/check_kubernetes.sh << EOF
#!/bin/bash
#****************************************************************#
# ScriptName: check_kubernetes.sh
# Create Date: 2020-06-23 22:19
#***************************************************************#

function chech_kubernetes() {
 for ((i=0;i<5;i++));do
  apiserver_pid_id=$(pgrep kube-apiserver)
  if [[ ! -z $apiserver_pid_id ]];then
   return
  else
   sleep 2
  fi
  apiserver_pid_id=0
 done
}

# 1:running  0:stopped
check_kubernetes
if [[ $apiserver_pid_id -eq 0 ]];then
 /usr/bin/systemctl stop keepalived
 exit 1
else
 exit 0
fi
EOF
```

根据上面的注意事项配置master2、master3节点的keepalived服务



#### （5）启动keeplived和haproxy服务

```go
systemctl enable --now keepalived haproxy
```

确保万一，查看一下服务状态，查看vip是否通

```go
systemctl status keepalived haproxy
ping 192.168.10.33
```



### 2.5、部署master节点

#### （1）加载镜像

解压后进入镜像目录，直接加载本地镜像即可，所有机器都需要加载

```
for i in `ll | grep -v grep | awk '{print $9}'`;do docker load -i $i; done
```



#### （2）初始化kubenetes的master1节点



所有节点都修改kubelet路径

```
cat > /etc/sysconfig/kubelet << EOF
KUBELET_EXTRA_ARGS="--root-dir=/home/k8s/kubelet"
EOF
```



执行如下命令，--control-plane-endpoint是vip和端口

```go
kubeadm init --control-plane-endpoint "192.168.10.33:8443" --upload-certs --v=6 --image-repository registry.aliyuncs.com/google_containers  --pod-network-cidr=10.244.0.0/16 --kubernetes-version 1.20.0


# 提示如下信息
[init] Using Kubernetes version: v1.18.3
[preflight] Running pre-flight checks
 [WARNING IsDockerSystemdCheck]: detected "cgroupfs" as the Docker cgroup driver. The recommended driver is "systemd". Please follow the guide at https://kubernetes.io/docs/setup/cri/
[preflight] Pulling images required for setting up a Kubernetes cluster
[preflight] This might take a minute or two, depending on the speed of your internet connection
[preflight] You can also perform this action in beforehand using 'kubeadm config images pull'
[kubelet-start] Writing kubelet environment file with flags to file "/var/lib/kubelet/kubeadm-flags.env"
[kubelet-start] Writing kubelet configuration to file "/var/lib/kubelet/config.yaml"
[certs] apiserver serving cert is signed for DNS names [master1 kubernetes kubernetes.default kubernetes.default.svc kubernetes.default.svc.cluster.local] and IPs [10.96.0.1 192.168.50.128 192.168.50.100]
...           # 省略
[upload-certs] Skipping phase. Please see --upload-certs
[mark-control-plane] Marking the node master1 as control-plane by adding the label "node-role.kubernetes.io/master=''"
[mark-control-plane] Marking the node master1 as control-plane by adding the taints [node-role.kubernetes.io/master:NoSchedule]
[addons] Applied essential addon: CoreDNS
[endpoint] WARNING: port specified in controlPlaneEndpoint overrides bindPort in the controlplane address
[addons] Applied essential addon: kube-proxy

Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

You can now join any number of control-plane nodes by copying certificate authorities
and service account keys on each node and then running the following as root:

  kubeadm join 192.168.10.33:8443 --token abcdef.0123456789abcdef \
    --discovery-token-ca-cert-hash sha256:4c738bc8e2684c5d52d80687d48925613b66ab660403649145eb668d71d85648 \
    --control-plane --certificate-key 4931f39d3f53351cb6966a9dcc53cb5cbd2364c6d5b83e50e258c81fbec69539 

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 192.168.10.33:8443 --token abcdef.0123456789abcdef \
    --discovery-token-ca-cert-hash sha256:4c738bc8e2684c5d52d80687d48925613b66ab660403649145eb668d71d85648
```

这个过程大概30s的时间就做完了，之所以初始化的这么快就是因为我们提前拉取了镜像。像我上面这样的没有报错信息，并且显示上面的最后10行类似的信息这些，说明我们的master1节点是初始化成功的

在使用集群之前还需要做些收尾工作，在master1节点执行：

```go
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

再配置一下环境变量

```go
cat >> ~/.bashrc <<EOF
export KUBECONFIG=/etc/kubernetes/admin.conf
EOF

source ~/.bashrc
```

好了，此时的master1节点就算是初始化完毕了

有个重要的点就是最后几行信息中，其中有两条kubeadm join 192.168.10.33:8443开头的信息，这分别是其他master节点和node节点加入kubernetes集群的认证命令，这个密钥是系统根据 sha256算法计算出来的，必须持有这样的密钥才可以加入当前的kubernetes集群



这两条加入集群的命令是有一些区别的：

比如这个第一条，我们看到最后有一行内容--control-plane --certificate-key xxxx，这是控制节点加入集群的命令，控制节点是kubernetes官方的说法，其实在我们这里指的就是其他的master节点

```go
kubeadm join 192.168.10.33:8443 --token abcdef.0123456789abcdef \
    --discovery-token-ca-cert-hash sha256:4c738bc8e2684c5d52d80687d48925613b66ab660403649145eb668d71d85648 \
    --control-plane --certificate-key 4931f39d3f53351cb6966a9dcc53cb5cbd2364c6d5b83e50e258c81fbec69539
```

而最后一条就表示node节点加入集群的命令，比如：

```go
kubeadm join 192.168.10.33:8443 --token abcdef.0123456789abcdef \
    --discovery-token-ca-cert-hash sha256:4c738bc8e2684c5d52d80687d48925613b66ab660403649145eb668d71d85648
```

所以这两个节点使用时要看清楚是什么类型的节点加入集群的



查看节点

如果此时查看当前集群的节点，会发现只有master1节点自己

```go
kubectl get node

# 结果
NAME      STATUS     ROLES    AGE     VERSION
master1   NotReady   master   9m58s   v1.20.0
```

接下来我们把其他两个master节点加入到kubernetes集群中



### 2.6、其他master节点加入kubernetes集群中

#### （1）master2节点加入集群

既然是其他的master节点加入集群，那肯定是使用如下命令：

```go
kubeadm join 192.168.10.33:8443 --token abcdef.0123456789abcdef \
     --discovery-token-ca-cert-hash sha256:4c738bc8e2684c5d52d80687d48925613b66ab660403649145eb668d71d85648 \
     --control-plane --certificate-key 4931f39d3f53351cb6966a9dcc53cb5cbd2364c6d5b83e50e258c81fbec69539


# 提示如下信息
[preflight] Running pre-flight checks
 [WARNING IsDockerSystemdCheck]: detected "cgroupfs" as the Docker cgroup driver. The 
......                                  #省略若干
[mark-control-plane] Marking the node master2 as control-plane by adding the taints [node-role.kubernetes.io/master:NoSchedule]

This node has joined the cluster and a new control plane instance was created:

To start administering your cluster from this node, you need to run the following as a regular user:

 mkdir -p $HOME/.kube
 sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
 sudo chown $(id -u):$(id -g) $HOME/.kube/config

Run 'kubectl get nodes' to see this node join the cluster.
```

看上去没有报错，说明加入集群成功，现在再执行一些收尾工作

```go
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

加环境变量

```go
cat >> ~/.bashrc <<EOF
export KUBECONFIG=/etc/kubernetes/admin.conf
EOF

source ~/.bashrc
```



#### （2）master3节点加入集群

```go
kubeadm join 192.168.10.33:8443 --token abcdef.0123456789abcdef \
     --discovery-token-ca-cert-hash sha256:4c738bc8e2684c5d52d80687d48925613b66ab660403649145eb668d71d85648 \
     --control-plane --certificate-key 4931f39d3f53351cb6966a9dcc53cb5cbd2364c6d5b83e50e258c81fbec69539
```

做一些收尾工作

```go
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

cat >> ~/.bashrc <<EOF
export KUBECONFIG=/etc/kubernetes/admin.conf
EOF

source ~/.bashrc
```

到此，所有的master节点都已经加入集群



查看集群master节点

```go
kubectl get node

# 结果
NAME      STATUS     ROLES    AGE     VERSION
master1   NotReady   master   25m     v1.20.0
master2   NotReady   master   12m     v1.20.0
master3   NotReady   master   3m30s   v1.20.0
```

你可以在任意一个master节点上执行kubectl get node查看集群节点的命令



### 2.7、node节点加入kubernetes集群中

正如我们上面所说的，master1节点初始化完成后，第二条kubeadm join xxx（或者说是最后一行内容）内容便是node节点加入集群的命令

```go
kubeadm join 192.168.10.33:8443 --token abcdef.0123456789abcdef \
    --discovery-token-ca-cert-hash sha256:4c738bc8e2684c5d52d80687d48925613b66ab660403649145eb668d71d85648
```

注意：node节点加入集群只需要执行上面的一条命令即可，只要没有报错就表示成功。不必像master一样做最后的加入环境变量等收尾工作



#### （1）node1节点加入集群

```go
kubeadm join 192.168.10.33:8443 --token abcdef.0123456789abcdef \
    --discovery-token-ca-cert-hash sha256:4c738bc8e2684c5d52d80687d48925613b66ab660403649145eb668d71d85648


# 提示如下信息
[preflight] Running pre-flight checks
 [WARNING IsDockerSystemdCheck]: detected "cgroupfs" as the Docker cgroup driver. The recommended driver is "systemd". Please follow the guide at https://kubernetes.io/docs/setup/cri/
[preflight] Reading configuration from the cluster...
....
....
[kubelet-start] Writing kubelet environment file with flags to file "/var/lib/kubelet/kubeadm-flags.env"
[kubelet-start] Starting the kubelet
[kubelet-start] Waiting for the kubelet to perform the TLS Bootstrap...

This node has joined the cluster:
* Certificate signing request was sent to apiserver and a response was received.
* The Kubelet was informed of the new secure connection details.

Run 'kubectl get nodes' on the control-plane to see this node join the cluster.
```

当看到倒数第四行内容This node has joined the cluster，这一行信息表示node1节点加入集群成功



#### （2）node2节点加入集群

```go
kubeadm join 192.168.10.33:8443 --token abcdef.0123456789abcdef \
--discovery-token-ca-cert-hash sha256:4c738bc8e2684c5d52d80687d48925613b66ab660403649145eb668d71d85648
```



#### （3）查看集群节点信息

此时我们可以在任意一个master节点执行如下命令查看此集群的节点信息

```go
kubectl get nodes

# 提示如下信息
NAME      STATUS     ROLES    AGE     VERSION
master1   NotReady   master   20h     v1.20.0
master2   NotReady   master   20h     v1.20.0
master3   NotReady   master   20h     v1.20.0
node1     NotReady   <none>   5m15s   v1.20.0
node2     NotReady   <none>   5m11s   v1.20.0
```

可以看到集群的五个节点都已经存在，但是现在还不能用，也就是说现在集群节点是不可用的，原因在于上面的第2个字段，我们看到五个节点都是NotReady状态，这是因为我们还没有安装网络插件

网络插件有calico，flannel等插件，这里我们选择使用flannel插件



### 2.8、安装网络插件



#### （1）生成文件

生成kube-flannel.yml文件

```go
cat >> ./kube-flannel.yml <<EOF
---
apiVersion: policy/v1beta1
kind: PodSecurityPolicy
metadata:
  name: psp.flannel.unprivileged
  annotations:
    seccomp.security.alpha.kubernetes.io/allowedProfileNames: docker/default
    seccomp.security.alpha.kubernetes.io/defaultProfileName: docker/default
    apparmor.security.beta.kubernetes.io/allowedProfileNames: runtime/default
    apparmor.security.beta.kubernetes.io/defaultProfileName: runtime/default
spec:
  privileged: false
  volumes:
    - configMap
    - secret
    - emptyDir
    - hostPath
  allowedHostPaths:
    - pathPrefix: "/etc/cni/net.d"
    - pathPrefix: "/etc/kube-flannel"
    - pathPrefix: "/run/flannel"
  readOnlyRootFilesystem: false
  # Users and groups
  runAsUser:
    rule: RunAsAny
  supplementalGroups:
    rule: RunAsAny
  fsGroup:
    rule: RunAsAny
  # Privilege Escalation
  allowPrivilegeEscalation: false
  defaultAllowPrivilegeEscalation: false
  # Capabilities
  allowedCapabilities: ['NET_ADMIN']
  defaultAddCapabilities: []
  requiredDropCapabilities: []
  # Host namespaces
  hostPID: false
  hostIPC: false
  hostNetwork: true
  hostPorts:
  - min: 0
    max: 65535
  # SELinux
  seLinux:
    # SELinux is unused in CaaSP
    rule: 'RunAsAny'
---
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: flannel
rules:
  - apiGroups: ['extensions']
    resources: ['podsecuritypolicies']
    verbs: ['use']
    resourceNames: ['psp.flannel.unprivileged']
  - apiGroups:
      - ""
    resources:
      - pods
    verbs:
      - get
  - apiGroups:
      - ""
    resources:
      - nodes
    verbs:
      - list
      - watch
  - apiGroups:
      - ""
    resources:
      - nodes/status
    verbs:
      - patch
---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: flannel
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: flannel
subjects:
- kind: ServiceAccount
  name: flannel
  namespace: kube-system
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: flannel
  namespace: kube-system
---
kind: ConfigMap
apiVersion: v1
metadata:
  name: kube-flannel-cfg
  namespace: kube-system
  labels:
    tier: node
    app: flannel
data:
  cni-conf.json: |
    {
      "name": "cbr0",
      "cniVersion": "0.3.1",
      "plugins": [
        {
          "type": "flannel",
          "delegate": {
            "hairpinMode": true,
            "isDefaultGateway": true
          }
        },
        {
          "type": "portmap",
          "capabilities": {
            "portMappings": true
          }
        }
      ]
    }
  net-conf.json: |
    {
      "Network": "10.244.0.0/16",
      "Backend": {
        "Type": "vxlan"
      }
    }
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-amd64
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - amd64
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: quay.io/coreos/flannel:v0.11.0-amd64
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: quay.io/coreos/flannel:v0.11.0-amd64
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
            add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-arm64
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - arm64
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: quay.io/coreos/flannel:v0.11.0-arm64
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: quay.io/coreos/flannel:v0.11.0-arm64
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
             add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-arm
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - arm
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: quay.io/coreos/flannel:v0.11.0-arm
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: quay.io/coreos/flannel:v0.11.0-arm
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
             add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-ppc64le
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - ppc64le
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: quay.io/coreos/flannel:v0.11.0-ppc64le
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: quay.io/coreos/flannel:v0.11.0-ppc64le
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
             add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-s390x
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - s390x
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: quay.io/coreos/flannel:v0.11.0-s390x
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: quay.io/coreos/flannel:v0.11.0-s390x
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
             add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
EOF
```



#### （2）安装flannel

在master节点执行此命令

```go
kubectl apply -f kube-flannel.yml 


# 提示如下信息
podsecuritypolicy.policy/psp.flannel.unprivileged created
clusterrole.rbac.authorization.k8s.io/flannel created
clusterrolebinding.rbac.authorization.k8s.io/flannel created
serviceaccount/flannel created
configmap/kube-flannel-cfg created
daemonset.apps/kube-flannel-ds-amd64 created
daemonset.apps/kube-flannel-ds-arm64 created
daemonset.apps/kube-flannel-ds-arm created
daemonset.apps/kube-flannel-ds-ppc64le created
daemonset.apps/kube-flannel-ds-s390x created
```

这样子就可以成功拉取flannel镜像了



查看flannel是否正常

如果你想查看flannel这些pod运行是否正常，使用如下命令

```go
kubectl get pods -n kube-system | grep flannel


# 提示如下信息
NAME                              READY   STATUS    RESTARTS   AGE
kube-flannel-ds-amd64-dp972       1/1     Running   0          66s
kube-flannel-ds-amd64-lkspx       1/1     Running   0          66s
kube-flannel-ds-amd64-rmsdk       1/1     Running   0          66s
kube-flannel-ds-amd64-wp668       1/1     Running   0          66s
kube-flannel-ds-amd64-zkrwh       1/1     Running   0          66s
```

如果第三字段STATUS不是处于Running状态的话，说明flannel是异常的，需要排查问题所在



查看节点是否为Ready

稍等片刻，执行如下指令查看节点是否可用

```go
kubectl get nodes


# 提示如下信息
NAME      STATUS   ROLES    AGE   VERSION
master1   Ready    master   21h   v1.20.0
master2   Ready    master   21h   v1.20.0
master3   Ready    master   21h   v1.20.0
node1     Ready    <none>   62m   v1.20.0
node2     Ready    <none>   62m   v1.20.0

# 允许master调度
kubectl taint nodes --all node-role.kubernetes.io/master-
```

目前节点状态是Ready，表示集群节点现在是可用的



### 2.9 安装local-path-storage



#### （1）创建文件

```
cat >> ./local-path-storage.yml <<EOF
apiVersion: v1
kind: Namespace
metadata:
  name: local-path-storage
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: local-path-provisioner-service-account
  namespace: local-path-storage
---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRole
metadata:
  name: local-path-provisioner-role
  namespace: local-path-storage
rules:
- apiGroups: [""]
  resources: ["nodes", "persistentvolumeclaims"]
  verbs: ["get", "list", "watch"]
- apiGroups: [""]
  resources: ["endpoints", "persistentvolumes", "pods"]
  verbs: ["*"]
- apiGroups: [""]
  resources: ["events"]
  verbs: ["create", "patch"]
- apiGroups: ["storage.k8s.io"]
  resources: ["storageclasses"]
  verbs: ["get", "list", "watch"]
---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRoleBinding
metadata:
  name: local-path-provisioner-bind
  namespace: local-path-storage
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: local-path-provisioner-role
subjects:
- kind: ServiceAccount
  name: local-path-provisioner-service-account
  namespace: local-path-storage
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: local-path-provisioner
  namespace: local-path-storage
spec:
  replicas: 1
  selector:
    matchLabels:
      app: local-path-provisioner
  template:
    metadata:
      labels:
        app: local-path-provisioner
    spec:
      serviceAccountName: local-path-provisioner-service-account
      containers:
      - name: local-path-provisioner
        image: rancher/local-path-provisioner:v0.0.4
        imagePullPolicy: IfNotPresent
        command:
        - local-path-provisioner
        - start
        - --config
        - /etc/config/config.json
        volumeMounts:
        - name: config-volume
          mountPath: /etc/config/
        env:
        - name: HELPER_IMAGE
          value: busybox:latest
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
      volumes:
        - name: config-volume
          configMap:
            name: local-path-config
---
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: local-path
  annotations:
    kubectl.kubernetes.io/last-applied-configuration: |
      {"apiVersion":"storage.k8s.io/v1","kind":"StorageClass","metadata":{"annotations":{},"name":"local-path"},"provisioner":"rancher.io/local-path","reclaimPolicy":"Delete","volumeBindingMode":"WaitForFirstConsumer"}
provisioner: rancher.io/local-path
volumeBindingMode: WaitForFirstConsumer
reclaimPolicy: Delete
---
kind: ConfigMap
apiVersion: v1
metadata:
  name: local-path-config
  namespace: local-path-storage
data:
  config.json: |-
        {
                "nodePathMap":[
                {
                        "node":"DEFAULT_PATH_FOR_NON_LISTED_NODES",
                        "paths":["/home/k8s"]
                }
                ]
        }
EOF
```



#### （2）安装local-path-storage

```
kubectl apply -f local-path-storage.yml
```



验证

```
kubectl get pod -n local-path-storage


# 提示如下信息
NAME                                      READY   STATUS    RESTARTS   AGE
local-path-provisioner-6c447d694c-jwrlt   1/1     Running   0          3m48s
```



#### （3）1.20及以上版本StorageClass创建PVC不成功解决

```
vi /etc/kubernetes/manifests/kube-apiserver.yaml

apiVersion: v1
···
    - --tls-private-key-file=/etc/kubernetes/pki/apiserver.key
    - --feature-gates=RemoveSelfLink=false # 添加这个配置
```



```
ps aux|grep kube-apiserver
kill -9 [Pid]
```



## 3、测试kubernetes集群



### 3.1、安装dashboard

#### （1）创建文件

```go
cat >> ./kubernetes-dashboard.yml <<EOF
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
rules:
  # Allow Metrics Scraper to get metrics from the Metrics server
  - apiGroups: ["metrics.k8s.io"]
    resources: ["pods", "nodes"]
    verbs: ["get", "list", "watch"]

---

kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: kubernetes-dashboard-reader
rules:
  # Allow Dashboard to create 'kubernetes-dashboard-key-holder' secret.
- apiGroups: [""]
  resources: ["secrets"]
  verbs: ["get","watch","list"]
  # Allow Dashboard to create 'kubernetes-dashboard-settings' config map.
- apiGroups: [""]
  resources: ["configmaps"]
  verbs: ["get","watch","list"]
  # Allow Dashboard to get, update and delete Dashboard exclusive secrets.
- apiGroups: [""]
  resources: ["secrets"]
  resourceNames: ["kubernetes-dashboard-key-holder"]
  verbs: ["get", "watch", "list"]
  # Allow Dashboard to get and update 'kubernetes-dashboard-settings' config map.
- apiGroups: [""]
  resources: ["configmaps"]
  resourceNames: ["kubernetes-dashboard-settings"]
  verbs: ["get", "watch","list"]
  # Allow Dashboard to get metrics from heapster.
- apiGroups: [""]
  resources: ["services"]
  resourceNames: ["heapster"]
  verbs: ["proxy"]
- apiGroups: [""]
  resources: ["services/proxy"]
  resourceNames: ["heapster", "http:heapster:", "https:heapster:"]
  verbs: ["get"]
- apiGroups: [""]
  resources: ["nodes"]
  verbs: ["get","watch","list"]
- apiGroups: [""]
  resources: ["persistentvolumes"]
  verbs: ["get","watch","list"]
- apiGroups: ["apps"]
  resources: ["daemonsets","replicasets"]
  verbs: ["get","watch","list"]
- apiGroups: ["rbac.authorization.k8s.io"]
  resources: ["roles","rolebindings","clusterroles","clusterrolebings"]
  verbs: ["get","watch","list"]
- apiGroups: ["storage.k8s.io"]
  resources: ["storageclasses"]
  verbs: ["get","watch","list"]
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: kubernetes-dashboard
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: kubernetes-dashboard
subjects:
  - kind: ServiceAccount
    name: kubernetes-dashboard
    namespace: kube-system

---

apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRoleBinding
metadata:
  name: admin-kubernetes-dashboard
  labels:
    k8s-app: kubernetes-dashboard
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: kubernetes-dashboard
  namespace: kube-system

---

apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: read-kubernetes-dashboard
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: kubernetes-dashboard-reader
subjects:
- kind: ServiceAccount
  name: kubernetes-dashboard
  namespace: kube-system
---
kind: ConfigMap
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-settings
  namespace: kube-system
---
kind: Deployment
apiVersion: apps/v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kube-system
spec:
  replicas: 1
  revisionHistoryLimit: 10
  selector:
    matchLabels:
      k8s-app: kubernetes-dashboard
  template:
    metadata:
      labels:
        k8s-app: kubernetes-dashboard
    spec:
      containers:
        - name: kubernetes-dashboard
          image: kubernetesui/dashboard:v2.0.0-beta8
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 8443
              protocol: TCP
          args:
            - --auto-generate-certificates
            - --namespace=kube-system
            # Uncomment the following line to manually specify Kubernetes API server Host
            # If not specified, Dashboard will attempt to auto discover the API server and connect
            # to it. Uncomment only if the default does not work.
            # - --apiserver-host=http://my-address:port
            - --enable-skip-login
          volumeMounts:
            - name: kubernetes-dashboard-certs
              mountPath: /certs
              # Create on-disk volume to store exec logs
            - mountPath: /tmp
              name: tmp-volume
          resources:
            limits:
              cpu: 4
              memory: 1G
            requests:
              cpu: 100m
              memory: 100Mi
          livenessProbe:
            httpGet:
              scheme: HTTPS
              path: /
              port: 8443
            initialDelaySeconds: 30
            timeoutSeconds: 30
          securityContext:
            allowPrivilegeEscalation: false
            readOnlyRootFilesystem: true
            runAsUser: 1001
            runAsGroup: 2001
      volumes:
        - name: kubernetes-dashboard-certs
          secret:
            secretName: kubernetes-dashboard-certs
        - name: tmp-volume
          emptyDir: {}
      serviceAccountName: kubernetes-dashboard
      nodeSelector:
        "beta.kubernetes.io/os": linux
      # Comment the following tolerations if Dashboard must not be deployed on master
      tolerations:
        - key: node-role.kubernetes.io/master
          effect: NoSchedule

---

kind: Deployment
apiVersion: apps/v1
metadata:
  labels:
    k8s-app: dashboard-metrics-scraper
  name: dashboard-metrics-scraper
  namespace: kube-system
spec:
  replicas: 1
  revisionHistoryLimit: 10
  selector:
    matchLabels:
      k8s-app: dashboard-metrics-scraper
  template:
    metadata:
      labels:
        k8s-app: dashboard-metrics-scraper
      annotations:
        seccomp.security.alpha.kubernetes.io/pod: 'runtime/default'
    spec:
      containers:
        - name: dashboard-metrics-scraper
          image: kubernetesui/metrics-scraper:v1.0.1
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 8000
              protocol: TCP
          livenessProbe:
            httpGet:
              scheme: HTTP
              path: /
              port: 8000
            initialDelaySeconds: 30
            timeoutSeconds: 30
          resources:
            limits:
              cpu: 4
              memory: 1G
            requests:
              cpu: 100m
              memory: 100Mi
          volumeMounts:
          - mountPath: /tmp
            name: tmp-volume
          securityContext:
            allowPrivilegeEscalation: false
            readOnlyRootFilesystem: true
            runAsUser: 1001
            runAsGroup: 2001
      serviceAccountName: kubernetes-dashboard
      nodeSelector:
        "beta.kubernetes.io/os": linux
      # Comment the following tolerations if Dashboard must not be deployed on master
      tolerations:
        - key: node-role.kubernetes.io/master
          effect: NoSchedule
      volumes:
        - name: tmp-volume
          emptyDir: {}
---
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kube-system
rules:
  # Allow Dashboard to get, update and delete Dashboard exclusive secrets.
  - apiGroups: [""]
    resources: ["secrets"]
    resourceNames: ["kubernetes-dashboard-key-holder", "kubernetes-dashboard-certs", "kubernetes-dashboard-csrf"]
    verbs: ["get", "update", "delete"]
    # Allow Dashboard to get and update 'kubernetes-dashboard-settings' config map.
  - apiGroups: [""]
    resources: ["configmaps"]
    resourceNames: ["kubernetes-dashboard-settings"]
    verbs: ["get", "update"]
    # Allow Dashboard to get metrics.
  - apiGroups: [""]
    resources: ["services"]
    resourceNames: ["heapster", "dashboard-metrics-scraper"]
    verbs: ["proxy"]
  - apiGroups: [""]
    resources: ["services/proxy"]
    resourceNames: ["heapster", "http:heapster:", "https:heapster:", "dashboard-metrics-scraper", "http:dashboard-metrics-scraper"]
    verbs: ["get"]
---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kube-system
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: kubernetes-dashboard
subjects:
  - kind: ServiceAccount
    name: kubernetes-dashboard
    namespace: kube-system
---
apiVersion: v1
kind: Secret
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-csrf
  namespace: kube-system
type: Opaque
data:
  csrf: ""

---

apiVersion: v1
kind: Secret
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-key-holder
  namespace: kube-system
type: Opaque

---

apiVersion: v1
kind: Secret
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-certs
  namespace: kube-system
type: Opaque
---
kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kube-system
spec:
  type: NodePort
  ports:
    - port: 443
      targetPort: 8443
      nodePort: 30000     # 访问端口
  selector:
    k8s-app: kubernetes-dashboard

---

kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: dashboard-metrics-scraper
  name: dashboard-metrics-scraper
  namespace: kube-system
spec:
  ports:
    - port: 8000
      targetPort: 8000
  selector:
    k8s-app: dashboard-metrics-scraper
---
apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kube-system
EOF
```



#### （2）安装dashboard

```go
kubectl apply -f kubernetes-dashboard.yml 


# 提示如下信息
namespace/kubernetes-dashboard created
serviceaccount/kubernetes-dashboard created
service/kubernetes-dashboard created
secret/kubernetes-dashboard-certs created
...
service/dashboard-metrics-scraper created
deployment.apps/dashboard-metrics-scraper created
```



查看dashboard运行是否正常

```go
kubectl get pods -n kube-system | grep dashboard


# 提示如下信息
NAME                                         READY   STATUS    RESTARTS   AGE
dashboard-metrics-scraper-694557449d-mlnl4   1/1     Running   0          2m31s
kubernetes-dashboard-9774cc786-ccvcf         1/1     Running   0          2m31s
```

主要是看status这一列的值，如果是Running，并且RESTARTS字段的值为0（只要这个值不是一直在渐渐变大），就是正常的，目前来看是没有问题的