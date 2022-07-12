package com.web.controller;

import com.web.service.CollectionService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/v1/collection")
@Api(tags = "集合")
@Slf4j
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @ApiOperation(value = "ConcurrentHashMap测试")
    @PostMapping("concurrentHashMap")
    public void concurrentHashMap() throws Exception {
        collectionService.concurrentHashMap();
    }

    @ApiOperation(value = "HashMap测试")
    @PostMapping("hashMap")
    public void hashMap() throws Exception {
        collectionService.hashMap();
    }

    @ApiOperation(value = "hashtable测试")
    @PostMapping("hashtable")
    public void hashtable() {
        Hashtable<String, Object> map = new Hashtable();
        map.put("a", "a");
    }

    @ApiOperation(value = "TreeMap测试")
    @PostMapping("treeMap")
    public void treeMap() {
        // 平衡二叉树
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("a", "a");
        map.get("a");
    }

    @ApiOperation(value = "LinkedList测试")
    @PostMapping("linkedList")
    public void linkedList() {
        LinkedList<String> list = new LinkedList<>();
        list.add("a");
    }

    @ApiOperation(value = "ArrayList测试")
    @PostMapping("arrayList")
    public void arrayList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("s");
        list.remove("s");
    }
    @ApiOperation(value = "vector测试")
    @PostMapping("vector")
    public void vector() {
        // 不能很好的控制
        Vector<String> list = new Vector<>();
        if (list.contains("a")) {
            list.add("a");
        }

        // 可以很好的控制
        synchronized (this) {
            if (list.contains("a")) {
                list.add("a");
            }
        }
    }

    @ApiOperation(value = "HashSet测试")
    @PostMapping("hashSet")
    public void hashSet() {
        HashSet<String> list = new HashSet<>();
        list.add("a");
    }

    @ApiOperation(value = "TreeSet测试")
    @PostMapping("treeSet")
    public void treeSet() {
        TreeSet<String> list = new TreeSet<>();
        list.add("a");
    }

    @ApiOperation(value = "copyOnWriteArrayList测试")
    @PostMapping("copyOnWriteArrayList")
    public void copyOnWriteArrayList() {
        // CopyOnWriteArrayList是ArrayList的一个线程安全的变体,
        // 其中所有可变操作（add、set等等）都是通过对底层数组进行一次新的复制来实现的
        // 这一般需要很大的开销, 但是当遍历操作的数量大大超过可变操作的数量时, 这种方法可能比其他替代方法更有效
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("");
        list.get(0);
        list.remove("");
    }

    @ApiOperation(value = "arrayBlockingQueue测试")
    @PostMapping("arrayBlockingQueue")
    public void arrayBlockingQueue() throws Exception {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        // 把 e 加到 BlockingQueue 里, 即如果 BlockingQueue 可以容纳, 则返回 true, 否则报异常
        queue.add("a");
        // 表示如果可能的话, 将 e 加到 BlockingQueue 里, 即如果 BlockingQueue 可以容纳, 则返回 true, 否则返回 false
        queue.offer("b");
        // 把 e 加到 BlockingQueue 里, 如果 BlockQueue 没有空间, 则调用此方法的线程被阻断直到 BlockingQueue 里面有空间再继续
        queue.put("c");
        // 取走 BlockingQueue 里排在首位的对象, 若 BlockingQueue 为空, 阻断进入等待状态直到 Blocking 有新的对象被加入为止
        queue.take();
        // 取走 BlockingQueue 里排在首位的对象, 若不能立即取出, 则可以等 time 参数规定的时间, 取不到时返回 null
        queue.poll(1, TimeUnit.SECONDS);
    }

    @ApiOperation(value = "linkedBlockingQueue测试")
    @PostMapping("linkedBlockingQueue")
    public void linkedBlockingQueue() throws Exception {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
        // 把 e 加到 BlockingQueue 里, 即如果 BlockingQueue 可以容纳, 则返回 true, 否则报异常
        queue.add("a");
        // 表示如果可能的话, 将 e 加到 BlockingQueue 里, 即如果 BlockingQueue 可以容纳, 则返回 true, 否则返回 false
        queue.offer("b");
        // 把 e 加到 BlockingQueue 里, 如果 BlockQueue 没有空间, 则调用此方法的线程被阻断直到 BlockingQueue 里面有空间再继续
        queue.put("c");
        // 取走 BlockingQueue 里排在首位的对象, 若 BlockingQueue 为空, 阻断进入等待状态直到 Blocking 有新的对象被加入为止
        queue.take();
        // 取走 BlockingQueue 里排在首位的对象, 若不能立即取出, 则可以等 time 参数规定的时间, 取不到时返回 null
        queue.poll(1, TimeUnit.SECONDS);
    }

    @ApiOperation(value = "synchronousQueue测试")
    @PostMapping("synchronousQueue")
    public void synchronousQueue() throws Exception {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        // 一个生产线程, 当它生产产品（即put的时候）, 如果当前没有人想要消费产品(即当前没有线程执行take),
        // 此生产线程必须阻塞, 等待一个消费线程调用take操作
        queue.put("c");
        // take操作将会唤醒该生产线程, 同时消费线程会获取生产线程的产品（即数据传递）
        queue.take();
    }

    @ApiOperation(value = "concurrentSkipListMap测试")
    @PostMapping("concurrentSkipListMap")
    public void concurrentSkipListMap() {
        ConcurrentSkipListMap<String, String> map = new ConcurrentSkipListMap<>();
        map.put("c", "c");
        map.get("c");
    }

    @ApiOperation(value = "快排")
    @PostMapping("quickSort")
    public List<Integer> quickSort(@RequestBody List<Integer> list) {
        collectionService.quickSort(list, 0, list.size() - 1);
        return list;
    }

}
