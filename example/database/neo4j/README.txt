# 安装
1. https://neo4j.com/business-subscription/?edition=enterprise&release=3.5.32&flavour=unix
2. 修改neo4j.conf, 将 default_listen_address=0.0.0.0 取消注释
3. 开放服务器7474、7687端口
4. 进入bin, 执行./neo4j start启动

#####################################

为对应的节点属性加上索引:
create index on:Disease(name);

#####################################

neo4j 查找两节点之间的路径:
# 两节点之间的所有路径
MATCH p=(a)-[*]->(b)
RETURN p

# a->b 直接连接
MATCH p=(a)-[]->(b)
RETURN p

# a-...>b a、b之间有三个关系及两个节点
# 等价于 (a) - () - () -> (b)
MATCH p=(a)-[*3]->(b)
RETURN p

# 路径包含2个以上关系
MATCH p=(a)-[*2..]->(b)
RETURN p

# 路径包含8个以内关系
MATCH p=(a)-[*..8]->(b)
RETURN p

# 路径包含3~5个关系
MATCH p=(a)-[*3..5]->(b)
RETURN p

match (n) detach delete n

#####################################