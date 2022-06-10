ansible 默认提供了很多模块来供我们使用,
在 Linux 中, 我们可以通过 ansible-doc -l 命令查看到当前 ansible 都支持哪些模块,
通过 ansible-doc  -s  模块名, 又可以查看该模块有哪些参数可以使用

我们常用的几个模块:
copy
file
cron
group
user
yum
service
script
ping
command
raw
get_url
synchronize


案例:
ansible '*' -m command -a 'uptime'
'*'：自己定义的主机       -m command：命令

# 指定节点上的权限, 属主和数组为root
ansible '*' -m file -a "dest=/tmp/t.sh mode=755 owner=root group=root"

# 指定节点上定义一个计划任务, 每隔3分钟到主控端更新一次时间
ansible '*' -m cron -a 'name="custom job" minute=*/3 hour=* day=* month=* weekday=* job="/usr/sbin/ntpdate 172.16.254.139"'

# 指定节点上创建一个组名为aaa, gid为2017的组
ansible all -m group -a 'gid=2017 name=a'

# 在节点上创建一个用户aaa, 组为aaa
ansible all -m user -a 'name=aaa groups=aaa state=present'

删除用户示例
ansible all -m user -a 'name=aaa groups=aaa remove=yes'

# 在节点上安装httpd
ansible all -m yum -a "state=present name=httpd"

# 在节点上启动服务，并开机自启动
ansible all -m service -a 'name=httpd state=started enabled=yes'

# 检查主机连接
ansible '*' -m ping

# 执行远程命令
ansible '*' -m command -a 'uptime'

# 执行主控端脚本
ansible '*' -m script -a '/root/test.sh'

# 执行远程主机的脚本
ansible '*' -m shell -a 'ps aux|grep zabbix'

# 类似shell
ansible '*' -m raw -a "ps aux|grep zabbix|awk '{print \$2}'"

# 创建软链接
ansible '*' -m file -a "src=/etc/resolv.conf dest=/tmp/resolv.conf state=link"

# 删除软链接
ansible '*' -m file -a "path=/tmp/resolv.conf state=absent"

# 复制文件到远程服务器
ansible '*' -m copy -a "src=/etc/ansible/ansible.cfg dest=/tmp/ansible.cfg owner=root group=root mode=0644"

# 在节点上运行hostname
nsible all -m raw -a 'hostname|tee'

# 将指定url上的文件下载到/tmp下
ansible all -m get_url -a 'url=http://10.1.1.116/favicon.ico dest=/tmp'

