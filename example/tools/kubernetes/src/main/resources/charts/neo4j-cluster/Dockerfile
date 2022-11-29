# https://go.neo4j.com/download-thanks.html?edition=community&release=5.2.0&flavour=unix&_gl=1*whwah*_ga*MTYyNTcxNzM4Mi4xNjY5NzE5NDUy*_ga_DL38Q8KGQC*MTY2OTcxOTQ1MC4xLjEuMTY2OTcyMDA4NC4wLjAuMA..&_ga=2.17067325.568178217.1669719454-1625717382.1669719452
# tar -zxvf neo4j-community-3.5.32-unix.tar.gz
# mv neo4j-community-3.5.32-unix neo4j
FROM centos:7
ADD jdk-8u152-linux-x64.tar.gz /
ENV JAVA_HOME /jdk1.8.0_152
ENV PATH $PATH:$JAVA_HOME/bin
COPY neo4j /neo4j