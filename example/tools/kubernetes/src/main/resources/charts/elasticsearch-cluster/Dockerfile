# https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.7.1-linux-x86_64.tar.gz
# tar -zxvf elasticsearch-7.7.1-linux-x86_64.tar.gz
# mv elasticsearch-7.7.1-linux-x86_64 elasticsearch
FROM centos:7
COPY elasticsearch /elasticsearch
RUN mkdir -p /elasticsearch/data && useradd es && chown -R es:es /elasticsearch
USER es