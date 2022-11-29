# https://dlcdn.apache.org/zookeeper/zookeeper-3.5.10/apache-zookeeper-3.5.10-bin.tar.gz
# tar -zxvf apache-zookeeper-3.5.10-bin.tar.gz
# mv apache-zookeeper-3.5.10-bin zookeeper
# cd zookeeper && mkdir zk_sasl_lib
# 将kafka的如下5个jar拷贝到zookeeper的zk_sasl_lib
# kafka-clients-2.4.1.jar
# lz4-java-1.6.0.jar
# slf4j-api-1.7.28.jar
# slf4j-log4j12-1.7.28.jar
# snappy-java-1.1.7.3.jar
FROM centos:7
ADD jdk-8u152-linux-x64.tar.gz /
ENV JAVA_HOME /jdk1.8.0_152
ENV PATH $PATH:$JAVA_HOME/bin
COPY zookeeper /zookeeper