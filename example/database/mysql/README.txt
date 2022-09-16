修改配置:
my.cnf
[mysqld]
log-bin=mysql-bin
server-id=1
bind-address=0.0.0.0
port=3306
basedir=/home/software/mysql-5.7.36
datadir=/home/software/mysql-5.7.36/data
socket=/tmp/mysql.sock
log-error=/home/software/mysql-5.7.36/logs/mysql.err
pid-file=/home/software/mysql-5.7.36/logs/mysql.pid

character_set_server=utf8mb4
symbolic-links=0
explicit_defaults_for_timestamp=true

启动命令:
./bin/mysqld_safe --defaults-file=./conf/my.cnf &

#####################################

1. 一条sql如何执行:
客户端 -> 连接器 -> 查询缓存 -> 分析器 -> 优化器 -> 执行器 -> 存储引擎

#####################################

2. 索引类型:
主键索引、唯一索引、普通索引、全文索引、覆盖索引、组合索引、冗余索引

force index 指定索引
#####################################

3.
B+树聚集索引的所有数据均存储在叶子节点, 而且数据是按照顺序排列的.
那么B+树使得范围查找、排序查找、分组查找以及去重查找变得异常简单

非聚集索引叶子节点中, 不在存储所有的数据了, 存储的是键值和主键

#####################################

sql关键字执行顺序:
FROM——ON——JOIN——WHERE——GROUP BY——SUM、COUNT——HAVING——SELECT——DISTINCT——ORDER BY——LIMIT

#####################################

mysql索引失效:
索引列不独立, 指不能是表达式的一部分, 不能是函数的参数
使用了左模糊
使用or查询部分字段没有使用索引
字符串条件没有使用''
不符合最左前缀原则的查询
索引字段没有添加not null约束
隐式转换导致索引失效, 条件类别不一致会隐式转换

#####################################

事务隔离级别
# 读未提交（READ UNCOMMITTED）
# 读提交 （READ COMMITTED）
# 可重复读 （REPEATABLE READ）
# 串行化 （SERIALIZABLE）

隔离级别     脏读      不可重复读      幻读
读未提交     可能      可能           可能
读提交       不可能    可能           可能
可重复读     不可能     不可能         可能
串行化       不可能     不可能         不可能

CREATE TABLE `isolation_test` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `isolation_test_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO isolation_test VALUES(1, 'b1');
INSERT INTO isolation_test VALUES(2, 'b2');
INSERT INTO isolation_test VALUES(3, 'b3');
INSERT INTO isolation_test VALUES(5, 'b5');
INSERT INTO isolation_test VALUES(6, 'b6');
INSERT INTO isolation_test VALUES(8, 'b8');
INSERT INTO isolation_test VALUES(9, 'b9');
INSERT INTO isolation_test VALUES(10, 'b10');
INSERT INTO isolation_test VALUES(11, 'b11');

查看当前会话隔离级别
SELECT @@tx_isolation;
mysql 查看是否自动提交的方法
show variables like 'autocommit';

脏读: 是指一个事务读取到了其他事务没有提交的数据.
设置会话A的事务隔离级别:
set session transaction isolation level read uncommitted;
会话都设置:
set autocommit=0;
start transaction;
A执行: select * from isolation_test;
B执行: update isolation_test set `name`='b99' where id=9; -- 不提交事务
回到会话A, 再次查询数据, 发现读取到了事务B修改后的数据, 但是事务B并没有提交, 这就是脏读


不可重复读: 是指一个事务内多次根据同一个查询条件查询出来的"同一行记录的值不一样".
设置会话A的事务隔离级别:
set session transaction isolation level read committed;
会话都设置:
set autocommit=0;
start transaction;
B执行: update isolation_test set `name`='b999' where id=9; -- 不提交事务
A执行: select * from isolation_test;
回到会话A, 进行数据查询, 发现查询的结果是b99, 而不是b999, 说明会话A并没有读取会话B事务没有提交的数据, 解决了脏读问题
B执行: COMMIT; -- 提交事务
A执行: select * from isolation_test;
回到会话A, 继续进行数据查询, 发现查出来的数据是b999, 在同一个事务中,
事务还没有结束, 同样的语句查出来不同的结果值, 这就是不可重复读


幻读: 是指一个事务内, 多次根据同个条件查出来的记录行数不一样.
设置会话A的事务隔离级别:
set session transaction isolation level repeatable read;
会话都设置:
set autocommit=0;
start transaction;
update isolation_test set `name`='b9999' where id=9;
打开会话B, 开启事务, 更新数据, 并且事务提交
回到会话A, 进行数据查询, 发现查出来的结果是b999, 两次查询结果一致, 解决了不可重复读问题
在会话A中执行对id=3的数据的更新操作, 然后进行数据查询, 出现了3条数据, 即多了一条id=3的数据, 这就是幻读


幻读及解决 (间隙锁):
两个客户端都关闭自动提交:
set autocommit=0;
start transaction;
客户端1执行: update gap_lock set name='bb' where id > 1 and id < 8;
客户端2执行: INSERT INTO gap_lock VALUES(7, 'b7');
此时, id > 1 and id < 8之间的数据被锁住, 不允许添加、删除、修改
客户端1执行: COMMIT;
客户端2执行: COMMIT; 才能提交成功.

间隙锁的目的是为了防止幻读, 其主要通过两个方面实现这个目的:
（1）防止间隙内有新数据被插入
（2）防止已存在的数据, 更新成间隙内的数据（例如防止numer=3的记录通过update变成number=5）

innodb自动使用间隙锁的条件:
（1）必须在RR(REPEATABLE READ)级别下
（2）检索条件必须有索引（没有索引的话，mysql会全表扫描,
那样会锁定整张表所有的记录, 包括不存在的记录, 此时其他事务不能修改不能删除不能添加）

串行化:
设置会话A的事务隔离级别:
set session transaction isolation level serializable;
两个客户端都关闭自动提交:
set autocommit=0;
start transaction;
打开会话A, 进行数据查询
打开会话B, 设置隔离级别为【串行化】, 开启事务,
进行数据插入操作, 发现并不能插入, 一直在等待, 从而解决了幻读问题（这种级别非常少用）

#####################################

MVCC实现原理:
MVCC是通过保存了数据库某个时间的快照来实现的. 也就是说当几个事务开启的时间不同,
可能会出现同一时刻、不同事务读取同一张表的同一行记录是不一样的, 这个机制也是可重复读的实现.

在InnoDB引擎的数据库中, 每一行记录后都有几个隐藏列来记录信息:
先了解一下两个概念:
系统版本号: 每当启动一个事务时, 系统版本号会递增.
事务版本号: 事务开始时的系统版本号作为该事务的版本号, 事务的版本号用于在select操作中与记录的DATA_TRX_ID字段做对比.

隐藏列:
DATA_TRX_ID:    记录了某行记录的系统版本号, 每当事务commit对该行的修改操作时就会将.
DATA_ROLL_PTR:  记录了此行记录的回滚记录指针, 找之前的历史版本就是通过它.
DELETE BIT:     标记此记录是否正在有事务删除它, 最后真正的删除操作是在事务commit后.

增删改查中的MVCC操作:
select:
1. 执行select操作时, InnoDB会查找到对应的数据行, 并对比DATA_TRX_ID（版本号）,
要求数据行的版本必须小于等于事务的版本, 如果当前数据行版本大于此事务版本, 那么InnoDB会进入undo log中查找,
确保当前事务读取的是事务之前存在的, 或者是由当前事务创建或修改的行.
2. InnoDB会查找到对应的数据行后, 查看DELETE BIT是否被定义, 只允许未定义, 或者删除的版本要大于此事务版本号.
保证在执行此事务之前还未被删除, 当且仅当这两个条件都成立才允许返回select结果.

insert: InnoDB创建新记录, 并以当前系统的版本号为新增记录的DATA_TRX_ID, 如果需要回滚则丢弃undo log.

delete: InnoDB寻找到需要删除的记录, 将此记录的DELETE BIT设置为系统当前版本号,
若事务回滚则去除DELETE BIT定义的版本号, 若事务提交则删除行.

update: InnoDB寻找到需要更新的行记录, 复制了一条新的记录, 新记录的版本ID为当前系统版本号,
新记录的回滚指针指向原记录, 将原记录的删除ID也设置为当前系统版本号. 提交后则删除原记录,
若回滚则删除复制的记录, 并清除原记录的删除ID.

MVCC锁相关:
快照读: 读取的是记录的可见版本, 不加锁.
当前读: 读取的是记录的最新版本, 并且会对读取的记录加上锁（有共享和排他锁）, 确保其他事务不会并发地修改这条记录.

快照读: 简单的select操作属于快照读, 不会加锁. select * from table where id=1;
当前读: 添加了关键字的特殊查询操作, 或者update、delete、insert都属于当前读, 需要加锁.
select * from table where ? lock in share mode;
select * from table where ? for update;
insert into table values (…);
update table set ? where ?;
delete from table where ?;
以上语句中除了第一条是共享锁（S锁）, 其他都是排他锁（X锁）

#####################################

MySQL中有六种日志文件:
重做日志redolog:
由引擎层的InnoDB引擎实现, 是物理日志, 记录的是物理数据页修改的信息, 比如"某个数据页上内容发生了哪些改动"
原理: 当一条数据需要更新时, InnoDB会先将更新操作记录到redolog中, 并更新到内存中, 这个更新就算是完成了
InnoDB引擎会在mysql空闲时将这些更新操作更新到磁盘中(数据文件)
存储: redolog是顺序写入指定大小的物理文件中的, 是循环写入的, 当文件快写满时, 会边擦除边刷磁盘,
即擦除日志记录(redolog file)并将数据刷到磁盘中
作用:
1.提供crash-safe 能力(崩溃恢复), 确保事务的持久性, 数据库突然崩溃, 有些数据并未刷到数据文件中,
当重启MySQL数据库, 会从redolog中未刷到磁盘的数据刷到磁盘中
2.利用WAL技术推迟物理数据页的刷新, 从而提升数据库吞吐, 有效降低了访问时延

回滚日志undolog:
由引擎层的InnoDB引擎实现, 是逻辑日志, 记录数据修改被修改前的值, 比如"把Name='B'修改为Name = 'B2',
那么undo日志就会用来存放Name='B'的记录"
当一条数据需要更新前, 会先把修改前的记录存储在undolog中, 如果这个修改出现异常, 则会使用undo日志来实现回滚操作, 保证事务的一致性
当事务提交之后, undolog并不能立马被删除, 而是会被放到待清理链表中, 待判断没有事物用到该版本的信息时才可以清理相应undolog
作用:
保存了事务发生之前的数据的一个版本, 用于回滚, 同时可以提供多版本并发控制下的读（MVCC）, 也即非锁定读

二进制日志binlog:
binlog会写入指定大小的物理文件中, 是追加写入的, 当前文件写满则会创建新的文件写入
产生: 事务提交的时候, 一次性将事务中的sql语句, 按照一定的格式记录到binlog中
清理: 可设置参数expirelogsdays, 在生成时间超过配置的天数之后, 会被自动删除
作用:
1.用于复制, 在主从复制中, 从库利用主库上的binlog进行重播(执行日志中记录的修改逻辑), 实现主从同步。
2.用于数据库的基于时间点的还原

3种记录模式:
statement: 基于SQL语句的模式, 某些语句中含有一些函数, 例如UUID,NOW等在复制过程可能导致数据不一致甚至出错
row: 基于行的模式, 记录的是行的变化, 很安全, 但是binlog的磁盘占用会比其他两种模式大很多, 在一些大表中清除大量数据时在binlog中会生成很多条语句, 可能导致从库延迟变大
mixed: 混合模式, 根据语句来选用是statement还是row模式, 表结构变更使用statement模式来记录, 如果SQL语句是update或者delete语句, 那么使用row模式

#####################################

表分区
CREATE TABLE `ota_order_2020` (........) ENGINE=InnoDB DEFAULT CHARSET=utf8
PARTITION BY RANGE (to_days(create_time)) (
PARTITION p201808 VALUES LESS THAN (to_days('2018-09-01')),
PARTITION p201809 VALUES LESS THAN (to_days('2018-10-01')),
PARTITION p201810 VALUES LESS THAN (to_days('2018-11-01')));

#####################################

复合索引:

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_name_age_sex` (`name`,`age`,`sex`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (1, '1', 1, 1, '1');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (2, '2', 2, 2, '2');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (3, '3', 3, 1, '3');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (4, '4', 4, 2, '4');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (5, '5', 5, 1, '5');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (6, '6', 6, 2, '6');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (7, '7', 7, 1, '7');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (8, '8', 8, 2, '8');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (9, '9', 9, 1, '9');
INSERT INTO `test`.`user`(`id`, `name`, `age`, `sex`, `address`) VALUES (10, '10', 10, 2, '10');

select_type   |  type    |   key                |    rows    |  filtered  |  Extra

# explain select * from user where name = '1';
# SIMPLE      |  ref     |  user_name_age_sex   |     1      |   100.00   |   null

# explain select name,age,sex from user where age = 1;
# SIMPLE      |  ref     |  user_name_age_sex   |     10     |   10.00    | Using where; Using index

# explain select * from user where name = '1' and age = 1 and sex = 1;
# SIMPLE      |  ref     |  user_name_age_sex   |     1      |   100.00   | null

# explain select * from user where name = '1' and sex = 1;
# SIMPLE      |  ref     |  user_name_age_sex   |     1      |   10.00    | null

# explain select * from user where sex = 1;
# SIMPLE      |  ALL     |                      |     10     |   10.00    | Using where

#####################################

order by 和 limit 用法

#####################################

Linux安装mysql步骤:

1. 下载地址
https://mirrors.cloud.tencent.com/mysql/downloads/MySQL-5.7/mysql-5.7.34-el7-x86_64.tar.gz

#####################################

最左匹配原则的原理:
一个形如(a,b,c)联合索引的 b+ 树, 其中的非叶子节点存储的是第一个关键字的索引 a,
而叶子节点存储的是三个关键字的数据. 这里可以看出 a 是有序的, 而 b、c 都是无序的.
但是当在 a 相同的时候, b 是有序的, b 相同的时候, c 又是有序的.
通过对联合索引的结构的了解, 那么就可以很好的了解为什么最左匹配原则中如果遇到范围查询就会停止了.
以 select * from t where a=5 and b>0 and c =1;
这样a,b可以用到（a,b,c）, c不可以 为例子, 当查询到 b 的值以后（这是一个范围值）,
c 是无序的, 所以就不能根据联合索引来确定到低该取哪一行.

为什么要使用联合索引:
1、减少开销. 建一个联合索引(col1,col2,col3),
实际相当于建了(col1),(col1,col2),(col1,col2,col3)三个索引, 每多一个索引,
都会增加写操作的开销和磁盘空间的开销. 对于大量数据的表, 使用联合索引会大大的减少开销.

2、覆盖索引. 对联合索引(col1,col2,col3), 如果有如下的sql:
select col1,col2,col3 from test where col1=1 and col2=2.
那么MySQL可以直接通过遍历索引取得数据, 而无需回表, 这减少了很多的随机io操作.
减少io操作, 特别的随机io其实是dba主要的优化策略,
所以, 在真正的实际应用中, 覆盖索引是主要的提升性能的优化手段之一.

3、效率高. 索引列越多, 通过索引筛选出的数据越少. 有1000W条数据的表, 有如下sql:
select * from table where col1=1 and col2=2 and col3=3, 假设假设每个条件可以筛选出10%的数据,
如果只有单值索引, 那么通过该索引能筛选出1000W10%=100w条数据,
然后再回表从100w条数据中找到符合col2=2 and col3= 3的数据, 然后再排序, 再分页,
如果是联合索引, 通过索引筛选出1000w10% 10% *10%=1w, 效率提升可想而知.

#####################################

mysql 使用B+树索引优势
B+树与B树的不同:
B+树非叶子节点不存在数据只存索引, B树非叶子节点存储数据
B+树查询效率更高. B+树使用双向链表串连所有叶子节点,
区间查询效率更高（因为所有数据都在B+树的叶子节点, 扫描数据库 只需扫一遍叶子结点就行了）,
但是B树则需要通过中序遍历才能完成查询范围的查找.
B+树查询效率更稳定. B+树每次都必须查询到叶子节点才能找到数据, 而B树查询的数据可能不在叶子节点,
也可能在, 这样就会造成查询的效率的不稳定
B+树的磁盘读写代价更小. B+树的内部节点并没有指向关键字具体信息的指针,
因此其内部节点相对B树更小, 通常B+树矮更胖, 高度小查询产生的I/O更少

hash索引范围查找性能差

#####################################

一个Update操作的具体流程:
当Update SQL被发给MySQL后, MySQL Server会根据where条件, 读取第一条满足条件的记录,
然后InnoDB引擎会将第一条记录返回, 并加锁 (current read).
待MySQL Server收到这条加锁的记录之后, 会再发起一个Update请求, 更新这条记录.
一条记录操作完成, 再读取下一条记录, 直至没有满足条件的记录为止. 因此, Update操作内部, 就包含了一个当前读.
同理, Delete操作也一样. Insert操作会稍微有些不同, 简单来说, 就是Insert操作可能会触发Unique Key的冲突检查, 也会进行一个当前读.

#####################################

mysql优化器:
1. 关联子查询
MySQL的子查询实现的非常糟糕. 最糟糕的一类查询时where条件中包含in()的子查询语句.
因为MySQL对in()列表中的选项有专门的优化策略， 一般会认为MySQL会先执行子查询返回所有in()子句中查询的值.
一般来说, in()列表查询速度很快, 所以我们会以为sql会这样执行:
select * from tast_user where id in (select id from user where name like '王%');
我们以为这个sql会解析成下面的形式
select * from tast_user where id in (1,2,3,4,5);
实际上MySQL是这样解析的
select * from tast_user where exists
(select id from user where name like '王%' and tast_user.id = user.id);
这时候由于子查询用到了外部表中的id字段所以子查询无法先执行. 通过explin可以看到, MySQL先选择对tast_user表进行全表扫描,
然后根据返回的id逐个执行子查询. 如果外层是一个很大的表, 那么这个查询的性能会非常糟糕.

2. UNION的限制
有时, MySQL无法将限制条件从外层下推导内层, 这使得原本能够限制部分返回结果的条件无法应用到内层查询的优化上.
如果希望union的各个子句能够根据limit只取部分结果集, 或者希望能够先排好序在合并结果集的话, 就需要在union的各个子句中分别使用这些子句.
例如: 想将两个子查询结果联合起来, 然后在取前20条, 那么MySQL会将两个表都存放到一个临时表中, 然后在去除前20行.
(select first_name,last_name from actor order by last_name) union all
(select first_name,last_name from customer order by  last_name) limit 20;
这条查询会将actor中的记录和customer表中的记录全部取出来放在一个临时表中, 然后在取前20条,
可以通过在两个子查询中分别加上一个limit 20来减少临时表中的数据.

3. 索引合并优化
前面文章中已经提到过, MySQL能够访问单个表的多个索引以合并和交叉过滤的方式来定位需要查找的行

4. 等值传递
某些时候, 等值传递会带来一些意想不到的额外消耗. 例如: 有一个非常大的in()列表, 而MySQL优化器发现存在where/on或using的子句, 将这个列表的值和另一个表的某个列相关联.
那么优化器会将in()列表都赋值应用到关联的各个表中. 通常, 因为各个表新增了过滤条件, 优化器可以更高效的从存储引擎过滤记录. 但是如果这个列表非常大, 则会导致优化和执行都会变慢.

5. 并行执行
MySQL无法利用多核特性来并行执行查询. 很多其他的关系型数据库鞥能够提供这个特性, 但MySQL做不到. 这里特别指出是想提醒大家不要花时间去尝试寻找并行执行查询的方法.

6. 哈希关联
在2013年MySQL并不执行哈希关联, MySQL的所有关联都是嵌套循环关联. 不过可以通过建立一个哈希索引来曲线实现哈希关联如果使用的是Memory引擎,
则索引都是哈希索引, 所以关联的时候也类似于哈希关联. 另外MariaDB已经实现了哈希关联.

7. 松散索引扫描
如果不遵守最左原则, where b = xxx, 就会使用紧凑索引遍历的方式, 进行全表扫表.
而我们如果真的有些情况, 需要查出符合 B 条件但对 A 不进行限制的情况, 应该如何处理, 可能有些同学会新开一个独立索引仅包含 B 列,
这样虽然能达到我们的目的, 但难免有些多余. mysql 也是也能很好的进行跳跃松散索引扫描, 只是 mysql 优化器无法自动进行, 必须在查询语句中进行必要的声明.
select * from xxx where B = xxx group by A;
添加 group by 字段后, 会先根据 A 索引分组后, 会在每个 A 的范围内使用索引进行快速查询定位所需要的 B 列, 这就叫做松散索引扫描.
比新建一个索引的效率会慢 A 的 distinct 倍, 但省去了新索引的消耗.

8. 最大值和最小值优化
对于MIN()和MAX()查询, MySQL的优化做的并不好, 例:
select min(actor_id) from actor where first_name = 'wang';
因为在first_name字段上并没有索引, 因此MySQL将会进行一次全表扫描. 如果MySQL能够进行主键扫描, 那么理论上,
当MySQL读到第一个太满足条件的记录的时候就是我们需要的最小值了, 因为主键是严格按照actor_id字段的大小排序的, 但是MySQL这时只会做全表扫描,
我们可以通过show status的全表扫描计数器来验证这一点. 一个曲线优化办法就是移除min()函数, 然后使用limit 1来查询.

9. 在同一个表上查询和更新
MySQL不允许对同一张表同时进行查询和更新. 这并不是优化器的限制.
update table set cnt = (select count(*) from table as tb where tb.type = table.type);
这个sql虽然符合标准单无法执行, 我们可以通过使用生成表的形式绕过上面的限制, 因为MySQL只会把这个表当做一个临时表来处理.
update table inner join (select type,count(*) as cnt from table group by type) as tb using(type) set table.cnt = tb.cnt;

#####################################

InnoDB存储引擎的架构设计
InnoDB组件结构:
buffer pool: 缓冲池, 缓存磁盘的数据
redo log buffer: 记录对缓冲池的操作, 根据策略写入磁盘防止宕机但事务已经提交而丢失数据
undo log: 当对缓冲池的数据进行修改时, 在事务未提交的时候都可以进行回滚, 将旧值写入 undo 日志文件便于回滚,
此时缓冲池的数据与磁盘中的不一致, 是脏数据

1. Buffer Pool
InnoDB Buffer Pool除了有缓存页、描述数据块之外, 还有几个链表来管理Buffer Pool, 比如free链表、flush链表、LRU链表等等.
LRU链表: young区域和old区域.
默认情况下, 磁盘中存放的数据页的大小是16KB, 也就是说, 一页数据包含了16KB的内容,
而Buffer Pool中存放的缓存页大小是和磁盘上数据页的大小是一一对应的, 都是16KB.

假设现在有一条更新语句:
update users set name = 'lisi' where id = 1
需要更新到数据库, InnoDB会执行哪些操作呢?
首先, InnoDB会判读缓冲池里是否存在 id = 1 这条数据, 如果不存在则从磁盘中加载到缓冲池中,
而且还会对这行数据加独占锁, 防止多个sql同时修改这行数据.

2. undo 日志文件
假设 id = 1 这条数据name原来的值 name = ‘zhangsan’, 现在我们要更新为 name = ‘lisi’,
那么我们就需要把旧值name='zhangsan’和id=1这些信息写入到undo日志文件中.
对于熟悉数据库的同学来说都了解事务的概念, 在事务未提交之前, 所有操作都有可能进行回滚,
即可以把 name = ‘lisi’ 回滚到 name = ‘zhangsan’, 所以将更新前的值写到undo日志文件.

3. 更新buffer pool 数据
在undo日志文件写入完毕之后, 便开始更新内存中的这条数据.
把 id = 1 的 name = ‘zhangsan’ 更新为 name = ‘lisi’.
这时内存中的数据已经更新完毕, 但磁盘上的还没有变化, 此时出现了不一致的脏数据.
这时可能有一个疑问, 万一事务提交完成, 但MySQL服务宕机了,
而内存中的数据还没写入到磁盘, 是不是会造成数据丢失而造成sql执行数据前后不一致?

4. redo log buffer
在InnoDB结构中, 有一个 redo log buffer 缓冲区存放redo日志, 所谓redo日志,
例如: 把id=1, name='zhangsan’修改为name=‘lisi’ 便是一条日志.
但这时redo log buffer 还仅仅存在内存中, 没能实现MySQL宕机后的数据恢复.

5. 事务没提交, 数据库宕机后有影响吗?
其实并没有影响, 事务没有提交, 意味着执行没有成功, 就算MySQL崩溃或者宕机后,
内存中的 buffer pool 和 redo log buffer 修改过的数据都会丢失, 也并不影响数据前后的一致性.
如果事务提交失败, 那数据库的数据更加不会改变.

6. 提交事务, redo日志的配置策略
在提交事务时, redo日记会根据策略实现把redo日志从 redo log buffer 里写入磁盘. 策略通过 innoDB_flush_log_at_trx_commit 来配置.
(1)innoDB_flush_log_at_trx_commit的参数为0, 就算事务提交后, 也不会把redo日志写入磁盘, MySQL宕机后会内存中的数据会丢失.
(2)innoDB_flush_log_at_trx_commit的参数为1, 事务提交后, redo日志会从内存刷入磁盘, 只要事务提交成功, redo log 就必然存在磁盘里.
此时就算buffer pool 的数据没有刷进磁盘, 也可以从redo log 中得知修改过哪些数据, MySQL宕机重启后, 可以从redo日志中恢复修改的数据.
(3)innoDB_flush_log_at_trx_commit的参数为2, 事务提交后, redo log 仅仅停留在 os cache 中, 还没刷进磁盘,
万一此时服务宕机了, 那么os cache 中的数据也会丢失, 即使事务提交成功, 也会造成数据丢失.
看完这几种相信为了保证数据安全, 参数为1是最佳策略.

7. 事务的最终提交, binlog
binlog其实是属于MySQL Server 的日志文件, 而在这出提出是因为与redo log有着很大的关联.
(1)biglog 与 redo log的区别:
redo log: 记录的是偏物理性质重做日志, 比如 “对哪个数据页中的什么记录, 做了哪些修改”
binlog: 偏向于逻辑性的日志, 如：“对users表中的id=10的一行数据做了更新操作, 更新以后的值是什么”

(2)提交事务的时候同时写入binlog
在执行更新的同时, innoDB与执行器一直在交互, 包括加载数据到缓冲池, 写入undo日志文件,
更新内存数据, 写redo日志和刷入磁盘等, 而对binlog的写入也是由执行器执行.

(3)binlog日志刷盘策略分析
sync_binlog参数控制binlog的刷盘策略
sync_ binlog默认值是0, 提交事务后, 会把binlog日志存在 os cache 中, MySQL宕机后会造成os cache中数据的丢失
sync_binlog 值为1, 提交事务后, 把binlog日志直接刷入磁盘中.

(4)基于binlog 和 redo log 完成事务的提交
binlog写入磁盘后, 会把binlog日志文件所在的位置和文件名称都写入redo log日志文件中, 同时在redo log日志文件里写入一个commit标记.

(5)commit 标记有什么意义?
commit 标记意义着保持redo log 和 binlog 日志一致.
如果在步骤5或者步骤6, 事务提交开始, MySQL宕机了, redo log 中并没有commit标记, 都算事务提交失败.
意味着 commint 标记是事务最终提交成功.

8. buffer pool 脏数据刷入磁盘
脏数据刷入磁盘是由后台IO线程随机刷入磁盘的.
这时候考虑到, 在刷入磁盘之前, MySQL宕机怎么办? 这时候, 事务已经提交成功, redo log 中也有commit标记,
就算宕机了, 重启后, 也会根据redo日志文件把数据更新到内存中, 等待IO线程的刷盘.

#####################################

show global variables;

1. 在线调整Buffer Pool大小:
SET GLOBAL innodb_buffer_pool_size=402653184;

2. 查看Buffer Pool的状态信息
show engine innodb status;

3. Buffer Pool中chunk调整
innodb_buffer_pool_size必须是innodb_buffer_pool_chunk_size × innodb_buffer_pool_instances的倍数

4. 修改连接数
set GLOBAL max_connections=500;

5.
show status like 'Threads%';

#####################################

mysql主备搭建:

修改主备auto.cnf的server-uuid不重复

主my.cnf增加:
server-id=1
log-bin=mysql-bin
binlog-ignore-db=mysql
binlog_ignore_db=information_schema
binlog_ignore_db=performation_schema
binlog_ignore_db=sys
binlog_format=MIXED
重启

备my.cnf增加:
server-id=2
log-bin=mysql-bin
binlog-ignore-db=mysql
binlog_ignore_db=information_schema
binlog_ignore_db=performation_schema
binlog_ignore_db=sys
binlog_format=MIXED
重启

备执行:
CHANGE MASTER TO MASTER_HOST='192.168.221.131',MASTER_USER='root',MASTER_PASSWORD='123',MASTER_LOG_FILE='mysql-bin.000001',MASTER_LOG_POS=0;
START SLAVE;
show slave status \G;
SHOW PROCESSLIST;

#####################################

mysql主主搭建:

修改主备auto.cnf的server-uuid不重复

主my.cnf增加:
server-id=1
log-bin=mysql-bin
binlog-ignore-db=mysql
binlog_ignore_db=information_schema
binlog_ignore_db=performation_schema
binlog_ignore_db=sys
binlog_format=MIXED
重启

备my.cnf增加:
server-id=2
log-bin=mysql-bin
binlog-ignore-db=mysql
binlog_ignore_db=information_schema
binlog_ignore_db=performation_schema
binlog_ignore_db=sys
binlog_format=MIXED
重启

备执行:
CHANGE MASTER TO MASTER_HOST='192.168.221.131',MASTER_USER='root',MASTER_PASSWORD='123',MASTER_LOG_FILE='mysql-bin.000001',MASTER_LOG_POS=0;
START SLAVE;
show slave status \G;
SHOW PROCESSLIST;

主执行:
CHANGE MASTER TO MASTER_HOST='192.168.221.132',MASTER_USER='root',MASTER_PASSWORD='123',MASTER_LOG_FILE='mysql-bin.000001',MASTER_LOG_POS=0;
START SLAVE;
show slave status \G;
SHOW PROCESSLIST;

#####################################

