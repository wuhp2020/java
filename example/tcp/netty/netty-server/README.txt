消息里为什么要有requestID:
如果使用netty的话, 一般会用channel.writeAndFlush()方法来发送消息二进制串,
这个方法调用后对于整个远程调用(从发出请求到接收到结果)来说是一个异步的, 即对于当前线程来说, 将请求发送出来后,
线程就可以往后执行了, 至于服务端的结果, 是服务端处理完成后, 再以消息的形式发送给客户端的. 于是这里出现以下两个问题:
1）怎么让当前线程“暂停”, 等结果回来后, 再向后执行？

2）如果有多个线程同时进行远程方法调用, 这时建立在client server之间的socket连接上会有很多双方发送的消息传递,
前后顺序也可能是随机的, server处理完结果后, 将结果消息发送给client, client收到很多消息, 怎么知道哪个消息结果是原先哪个线程调用的？

怎么解决呢:
1）client线程每次通过socket调用一次远程接口前, 生成一个唯一的ID,
即requestID（requestID必需保证在一个Socket连接里面是唯一的）, 一般常常使用AtomicLong从0开始累计数字生成唯一ID.

2）将处理结果的回调对象callback, 存放到全局ConcurrentHashMap里面put(requestID, callback).

3）当线程调用channel.writeAndFlush()发送消息后, 紧接着执行callback的get()方法试图获取远程返回的结果.
在get()内部, 则使用synchronized获取回调对象callback的锁, 再先检测是否已经获取到结果,
如果没有, 然后调用callback的wait()方法, 释放callback上的锁, 让当前线程处于等待状态.

4）服务端接收到请求并处理后, 将response结果（此结果中包含了前面的requestID）发送给客户端,
客户端socket连接上专门监听消息的线程收到消息, 分析结果, 取到requestID, 再从前面的ConcurrentHashMap里面get(requestID),
从而找到callback对象, 再用synchronized获取callback上的锁, 将方法调用结果设置到callback对象里,
再调用callback.notifyAll()唤醒前面处于等待状态的线程.

public Object get() {
    synchronized (this) { // 旋锁
        while (!isDone) { // 是否有结果了
            wait();       //没结果是释放锁, 让当前线程处于等待状态
        }
    }
}

private void setDone(Response res) {
    this.res = res;
    isDone = true;
    synchronized (this) { //获取锁, 因为前面wait()已经释放了callback的锁了
        notifyAll();      // 唤醒处于等待的线程
    }
}

#####################################


