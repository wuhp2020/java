# https://download.redis.io/releases/redis-5.0.14.tar.gz
# tar -zxvf redis-5.0.14.tar.gz
# mv redis-5.0.14 redis
# yum install gcc
# yum install gcc-c++
# cd redis/deps
# make hiredis jemalloc linenoise lua geohash-int
# cd ../
# make && make install PREFIX=../
FROM centos:7
COPY redis /redis