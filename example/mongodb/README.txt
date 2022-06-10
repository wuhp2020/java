启动命令:
./bin/mongod --port 27017 --dbpath ./data --fork --logpath ./logs/mongodb.log --bind_ip 0.0.0.0 &

#####################################

1. mongodb分片

#!/bin/sh

kill_cluster()
{
    kill -9 `netstat -tunlp | grep 21000|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 22000|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 23000|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 31000|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 32000|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 33000|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 41010|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 41020|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 41030|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 42010|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 42020|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 42030|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 43010|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 43020|awk '{print $NF}' | egrep -o "[0-9]+"`
    kill -9 `netstat -tunlp | grep 43030|awk '{print $NF}' | egrep -o "[0-9]+"`
}

remove_dir()
{
    rm -rf ./data &&\
    rm -rf ./logs &&\
    rm -rf ./pid
}

mkdir_dir()
{
    mkdir ./data/shard-1/master -p &&\
    mkdir ./data/shard-1/slave -p &&\
    mkdir ./data/shard-1/arbiter -p &&\
    mkdir ./data/shard-2/master -p &&\
    mkdir ./data/shard-2/slave -p &&\
    mkdir ./data/shard-2/arbiter -p &&\
    mkdir ./data/shard-3/master -p &&\
    mkdir ./data/shard-3/slave -p &&\
    mkdir ./data/shard-3/arbiter -p &&\
    mkdir ./data/config-server-1 -p &&\
    mkdir ./data/config-server-2 -p &&\
    mkdir ./data/config-server-3 -p &&\
    mkdir ./logs/shard-1 -p &&\
    mkdir ./logs/shard-2 -p &&\
    mkdir ./logs/shard-3 -p &&\
    mkdir ./pid/shard-1 -p &&\
    mkdir ./pid/shard-2 -p &&\
    mkdir ./pid/shard-3 -p
}

mongos_start()
{
    nohup ./bin/mongos --port 21000 --bind_ip 0.0.0.0 --configdb config-server/127.0.0.1:31000,127.0.0.1:32000,127.0.0.1:33000 --pidfilepath ./pid/mongos-1.pid --logpath ./logs/mongos-1.log --config ./conf/mongos.conf &
    nohup ./bin/mongos --port 22000 --bind_ip 0.0.0.0 --configdb config-server/127.0.0.1:31000,127.0.0.1:32000,127.0.0.1:33000 --pidfilepath ./pid/mongos-2.pid --logpath ./logs/mongos-2.log --config ./conf/mongos.conf &
    nohup ./bin/mongos --port 23000 --bind_ip 0.0.0.0 --configdb config-server/127.0.0.1:31000,127.0.0.1:32000,127.0.0.1:33000 --pidfilepath ./pid/mongos-3.pid --logpath ./logs/mongos-3.log --config ./conf/mongos.conf &
}

config_server_start()
{
    nohup ./bin/mongod --port 31000 --bind_ip 0.0.0.0 --configsvr --replSet config-server --dbpath ./data/config-server-1 --pidfilepath ./pid/config-server-1.pid --logpath ./logs/config-server-1.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 32000 --bind_ip 0.0.0.0 --configsvr --replSet config-server --dbpath ./data/config-server-2 --pidfilepath ./pid/config-server-2.pid --logpath ./logs/config-server-2.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 33000 --bind_ip 0.0.0.0 --configsvr --replSet config-server --dbpath ./data/config-server-3 --pidfilepath ./pid/config-server-3.pid --logpath ./logs/config-server-3.log --config ./conf/mongod.conf &
}

shard_start()
{
    nohup ./bin/mongod --port 41010 --bind_ip 0.0.0.0 --shardsvr --replSet shard-1 --dbpath ./data/shard-1/master --pidfilepath ./pid/shard-1/master.pid --logpath ./logs/shard-1/master.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 41020 --bind_ip 0.0.0.0 --shardsvr --replSet shard-1 --dbpath ./data/shard-1/slave --pidfilepath ./pid/shard-1/slave.pid --logpath ./logs/shard-1/slave.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 41030 --bind_ip 0.0.0.0 --shardsvr --replSet shard-1 --dbpath ./data/shard-1/arbiter --pidfilepath ./pid/shard-1/arbiter.pid --logpath ./logs/shard-1/arbiter.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 42010 --bind_ip 0.0.0.0 --shardsvr --replSet shard-2 --dbpath ./data/shard-2/master --pidfilepath ./pid/shard-2/master.pid --logpath ./logs/shard-2/master.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 42020 --bind_ip 0.0.0.0 --shardsvr --replSet shard-2 --dbpath ./data/shard-2/slave --pidfilepath ./pid/shard-2/slave.pid --logpath ./logs/shard-2/slave.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 42030 --bind_ip 0.0.0.0 --shardsvr --replSet shard-2 --dbpath ./data/shard-2/arbiter --pidfilepath ./pid/shard-2/arbiter.pid --logpath ./logs/shard-2/arbiter.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 43010 --bind_ip 0.0.0.0 --shardsvr --replSet shard-3 --dbpath ./data/shard-3/master --pidfilepath ./pid/shard-3/master.pid --logpath ./logs/shard-3/master.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 43020 --bind_ip 0.0.0.0 --shardsvr --replSet shard-3 --dbpath ./data/shard-3/slave --pidfilepath ./pid/shard-3/slave.pid --logpath ./logs/shard-3/slave.log --config ./conf/mongod.conf &
    nohup ./bin/mongod --port 43030 --bind_ip 0.0.0.0 --shardsvr --replSet shard-3 --dbpath ./data/shard-3/arbiter --pidfilepath ./pid/shard-3/arbiter.pid --logpath ./logs/shard-3/arbiter.log --config ./conf/mongod.conf &
}

kill_cluster
sleep 8
remove_dir
sleep 8
mkdir_dir
sleep 8
config_server_start
sleep 8
shard_start
sleep 8
mongos_start


#!/bin/sh

config-server
./bin/mongo --port 31000 --host 127.0.0.1
rs.initiate({_id: "config-server", configsvr: true, members:[{_id:0, host:'127.0.0.1:33000'}, {_id:1, host:'127.0.0.1:32000'}, {_id:2, host:'127.0.0.1:31000'}]});


shard-1
./bin/mongo --port 41010 --host 127.0.0.1
rs.initiate({_id:"shard-1", members:[{_id:0, host:'127.0.0.1:41030', arbiterOnly:true}, {_id:1, host:'127.0.0.1:41020'}, {_id:2, host:'127.0.0.1:41010'}]});

shard-2
./bin/mongo --port 42010 --host 127.0.0.1
rs.initiate({_id:"shard-2", members:[{_id:0, host:'127.0.0.1:42030', arbiterOnly:true}, {_id:1, host:'127.0.0.1:42020'}, {_id:2, host:'127.0.0.1:42010'}]});

shard-3
./bin/mongo --port 43010 --host 127.0.0.1
rs.initiate({_id:"shard-3", members:[{_id:0, host:'127.0.0.1:43030', arbiterOnly:true}, {_id:1, host:'127.0.0.1:43020'}, {_id:2, host:'127.0.0.1:43010'}]});


mongo
./bin/mongo --port 21000 --host 127.0.0.1
sh.addShard("shard-1/127.0.0.1:41010,127.0.0.1:41020,127.0.0.1:41030")
sh.addShard("shard-2/127.0.0.1:42010,127.0.0.1:42020,127.0.0.1:42030")
sh.addShard("shard-3/127.0.0.1:43010,127.0.0.1:43020,127.0.0.1:43030")


show dbs;
use city_shield_alert;
show collections;
use admin;
# 指定city_shield_alert分片生效
db.runCommand( { enablesharding :"city_shield_alert"})
# 指定数据库里需要分片的集合和片键
# id做片键, 其他字段不允许做唯一索引
# db.runCommand({shardcollection: "city_shield_alert.fs.files", key: {files_id: 1}})
db.runCommand({shardcollection: "city_shield_alert.fs.chunks", key: {files_id: "hashed"}})


#####################################

创建索引:
1. 基础索引
在字段age上创建索引, 1升序、-1降序
db.users.ensureIndex({age:1})
db.t3.ensureIndex({age:1} , {backgroud:true})

2. 文档索引
db.factories.insert( { name: "wwl", addr: { city: "Beijing", state: "BJ" } } );

3. 组合索引
db.factories.ensureIndex( { "addr.city" : 1, "addr.state" : 1 } );

4. 唯一索引
db.t4.ensureIndex({firstname: 1, lastname: 1}, {unique: true});

5. 强制使用索引
db.t5.find({age:{$lt:30}}).hint({name:1, age:1}).explain()

6. 删除索引
删除t3表中的所有索引
db.t3.dropIndexes()

7. 删除t4表中的firstname索引
db.t4.dropIndex({firstname: 1})

#####################################

性能监控工具
1. mongosniff
可以从底层监控到底有哪些命令发送给了MongoDB 去执行
./mongosniff --source NET lo

2. mongostat
可以快速的查看某组运行中的MongoDB实例的统计信息、字段说明:
insert: 每秒插入量
query: 每秒查询量
update: 每秒更新量
delete: 每秒删除量
locked: 锁定量
qr | qw: 客户端查询排队长度(读|写)
ar | aw: 活跃客户端量(读|写)
conn: 连接数
time: 当前时间

3. db.serverStatus
查看实例运行状态

4. db.stats
查看数据库状态信息


#####################################

为库创建用户名密码

# 登录mongos
mongo --port 27017

# 创建账户
use admin;
db.auth("root", "123");
# 切换到其它数据库, 创建相关的用户
use public-storage;
db.createUser({'user':'cx','pwd':'123','roles':[{'role':'readWrite','db':'public-storage'}]});
db.runCommand( { enablesharding :"public-storage"});
show users;

# 退出连接, 测试新建用户登录
use public-storage;
db.auth("cx", "123");