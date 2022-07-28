linux网络:
# 创建network namespace
ip netns add wuhp

# 查看network namespace
ip netns list

# 创建veth
ip link add wuhp0 type veth peer name wuhp1

# 查看veth
ip link list

# 添加veth到namespace
ip link set wuhp1 netns wuhp

# 通过命令查看wuhp中的veth
ip netns exec wuhp ip link list

# 配置network namespace的网口
ip netns exec wuhp ifconfig wuhp1 100.2.96.2/16 up
ip netns exec wuhp ip addr list

# 配置剩余的veth网口
ifconfig wuhp0 100.2.96.3/16 up
ip addr list

# 通信
ip netns exec wuhp bash
ping 100.2.96.3

#####################################

# 虚拟机net模式
BOOTPROTO=static
NAME=ens33
UUID=477ee6a0-e61d-4a66-aceb-f405913e8160
DEVICE=ens33
ONBOOT=yes
IPADDR=192.168.221.129
GATEWAY=192.168.221.2
NETMASK=255.255.255.0
DNS1=192.168.221.2

#####################################

java环境变量

vi /root/.bash_profile

JAVA_HOME=/home/software/jdk1.8.0_152
PATH=$PATH:$HOME/bin:$JAVA_HOME:$JAVA_HOME/bin

export JAVA_HOME
export PATH
