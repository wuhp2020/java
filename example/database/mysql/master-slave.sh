#!/bin/bash
# 这个脚本用来配置MySQL主从同步
# 日期：2018-12-17

#!/bin/bash
master_ip=192.168.100.12
slave_ip=192.168.100.13
mysqlc="mysql -uroot -p123"

check_ok()
{
    if [ $? -ne 0 ]
    then
        echo "$1 出错了。"
        exit 1
    fi
}

f_exist()
{
    d=`date +%F%T`
    if [ -f $1 ]
    then
        mv $1 $1_$d
    fi
}

## 设置主mysql配置
if ! grep '^server-id' /etc/my.cnf
then
    sed -i '/^\[mysqld\]$/a\server-id = 1001' /etc/my.cnf
fi

if ! grep '^log-bin.*=.*' /etc/my.cnf
then
    sed -i '/^\[mysqld\]$/a\log-bin = aminglinux' /etc/my.cnf
fi

sed -i '/^log-bin.*/a\binlog-ignore-db = mysql ' /etc/my.cnf

/etc/init.d/mysqld restart
check_ok "主上重启mysql"

## 登录mysql，授权用户、锁表以及show master status。
$mysqlc <<EOF
    grant replication slave on *.* to 'repl'@$slave_ip identified by 'yourpassword';
    flush tables with read lock;
EOF
$mysqlc -e "show master status" > /tmp/master.log
file=`tail -1 /tmp/master.log|awk '{print $1}'`
pos=`tail -1 /tmp/master.log|awk '{print $2}'`

## 创建在从上配置和操作的脚本
f_exist /tmp/slave.sh

cat > /tmp/slave.sh << EOF
#!/bin/bash
if ! grep '^server-id' /etc/my.cnf
then
    sed -i '/^\[mysqld\]$/a\server-id = 1002' /etc/my.cnf
fi

/etc/init.d/mysqld restart
check_ok "从上重启mysql"

$mysqlc  <<EOF
    stop slave;
    change master to master_host="$master_ip", master_user="repl", master_password="yourpassword", master_log_file="$file", master_log_pos=$pos;
    start slave;
EOF
EOF

## 创建传输slave.sh的expect脚本
f_exist /tmp/rs_slave.expect

cat > /tmp/rs_slave.expect <<EOF
#!/usr/bin/expect
set passwd "aminglinux"
spawn rsync -a /tmp/slave.sh root@$slave_ip:/tmp/slave.sh
expect {
    "yes/no" { send "yes\r"}
    "password:" { send "\$passwd\r" }
}
expect eof
EOF

## 执行expect脚本
chmod +x /tmp/rs_slave.expect
/tmp/rs_slave.expect
check_ok "传输slave.sh"

## 创建远程执行命令的expect脚本
f_exist /tmp/exe.expect

cat > /tmp/exe.expect <<EOF
#!/usr/bin/expect
set passwd "aminglinux"
spawn ssh root@$slave_ip
expect {
    "yes/no" { send "yes\r"}
    "password:" { send "\$passwd\r" }
}
expect "]*"
send "/bin/bash /tmp/slave.sh\r"
expect "]*"
send "exit\r"
EOF

## 执行expect脚本
chmod +x /tmp/exe.expect
/tmp/exe.expect
check_ok "远程执行slave.sh"

## 主上解锁表
$mysqlc -e "unlock tables"