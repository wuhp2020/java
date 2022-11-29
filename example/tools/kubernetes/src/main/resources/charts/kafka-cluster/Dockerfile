# https://archive.apache.org/dist/kafka/2.8.2/kafka_2.13-2.8.2.tgz
# tar -zxvf kafka_2.13-2.8.2.tgz
# mv kafka_2.13-2.8.2 kafka
FROM centos:7
ADD jdk-8u152-linux-x64.tar.gz /
ENV JAVA_HOME /jdk1.8.0_152
ENV PATH $PATH:$JAVA_HOME/bin
COPY kafka /kafka