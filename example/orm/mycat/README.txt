mycat是基于Java开发的, 确保安装好了java环境:
解压即用
生产环境线程数=CPU的核数, 推荐使用mycat1.6版本
在conf/wrapper.conf配置文件中修改JVM参数
在conf/server.xml中修改user和password, 默认的服务端口8066, 可修改
./mycat start 启动
./mycat stop 停止
./mycat console 前台运行
./mycat install 添加到系统自动启动（暂未实现）
./mycat remove 取消随系统自动启动（暂未实现）
./mycat restart 重启服务
./mycat pause 暂停
./mycat status 查看启动状态

#####################################

配置切分规则:
1. 修改mycat/conf/schema.xml的内容
<?xml version="1.0"?>
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">

    <schema name="TESTDB" checkSQLschema="false" sqlMaxLimit="100">
            <table name="user" primaryKey="id" dataNode="dn01,dn02" rule="rule1" />
    </schema>

    <!-- 设置dataNode 对应的数据库,及 mycat 连接的地址dataHost -->
    <dataNode name="dn01" dataHost="dh01" database="db01" />
    <dataNode name="dn02" dataHost="dh01" database="db02" />

    <!-- mycat 逻辑主机dataHost对应的物理主机.其中也设置对应的mysql登陆信息 -->
    <dataHost name="dh01" maxCon="1000" minCon="10" balance="0" writeType="0" dbType="mysql" dbDriver="native">
            <heartbeat>select user()</heartbeat>
            <writeHost host="server1" url="127.0.0.1:3306" user="root" password="WolfCode_2017"/>
    </dataHost>
</mycat:schema>

<schema>: 表示的是在mycat中的逻辑库配置, 逻辑库名称为:TESTDB
<table>: 表示在mycat中的逻辑表配置，逻辑表名称为:user, 映射到两个数据库节点dataNode中, 切分规则为:rule1(在rule.xml配置)
<dataNode>: 表示数据库节点, 这个节点不一定是单节点, 可以配置成读写分离
<dataHost>: 真实的数据库的地址配置
<heartbeat>: 用户心跳检测
<writeHost>: 写库的配置

2. 修改mycat/conf/rule.xml的内容
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mycat:rule SYSTEM "rule.dtd">
<mycat:rule xmlns:mycat="http://io.mycat/">
    <tableRule name="rule1">
        <rule>
            <columns>id</columns>
            <algorithm>mod-long</algorithm>
        </rule>
    </tableRule>
    <function name="mod-long" class="io.mycat.route.function.PartitionByMod">
        <!-- how many data nodes -->
        <property name="count">2</property>
    </function>
    <table name="user" primaryKey="id" dataNode="dn01,dn02" rule="rule1" />
</mycat:rule>

