package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class LockService {

    @Autowired
    private ZkClient zkClient;

    public static final String lock = "/wuhp";

    private ThreadLocal<Integer> reentrantCount = new ThreadLocal();
    private ThreadLocal<String> currentPath = new ThreadLocal();
    private ThreadLocal<String> beforePath = new ThreadLocal();

    public void queueLock() throws Exception {
        try {
            lock();
            log.info("获取锁 =====");
        } finally {
            unlock();
            log.info("释放锁 =====================\n");
        }
    }

    private void lock() throws Exception {
        if(!tryLock()) {
            // 阻塞
            waitLock();
            // 再次获取锁
            this.lock();
        }
    }

    private void unlock() throws Exception {
        if(currentPath.get() != null) {
            int count = reentrantCount.get()==null?0:reentrantCount.get();
            if(count > 1) {
                log.info("可重入锁 =============" + Thread.currentThread().getName());
                reentrantCount.set(--count);
                return;
            } else {
                reentrantCount.set(null);
            }
        }
        log.info("删除节点 =============" + Thread.currentThread().getName());

        zkClient.delete(currentPath.get());
        currentPath.set(null);
    }

    private boolean tryLock() {
        if (!zkClient.exists(lock)) {
            try {
                zkClient.createPersistent(lock);
            } catch (Exception e) {
                log.info("已经存在");
            }
        }

        // 可重复锁
        if (reentrantCount.get() != null) {
            int count = reentrantCount.get();
            if (count > 0) {
                reentrantCount.set(++count);
                return true;
            }
        }

        // 创建临时有序
        if (currentPath.get() == null) {
            currentPath.set(zkClient.createEphemeralSequential(lock + "/", "a"));
        }

        // 获取所有子节点
        List<String> children = zkClient.getChildren(lock);

        // 排序
        Collections.sort(children);

        // 判断当前节点是否最小
        if (currentPath.get().equals(lock + "/" + children.get(0))) {
            reentrantCount.set(1);
            return true;
        } else {
            // 取前一个
            // 得到字节的索引
            int currentIndex = children.indexOf(currentPath.get().substring(lock.length() + 1));
            beforePath.set(lock + "/" + children.get(currentIndex -1));
        }
        return false;
    }

    private void waitLock() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                latch.countDown();
            }
        };

        zkClient.subscribeDataChanges(beforePath.get(), listener);
        // 让自己阻塞
        if (zkClient.exists(beforePath.get())) {
            try {
                latch.await();
            } catch (Exception e) {
                log.info("===");
            }
        }

        zkClient.unsubscribeDataChanges(beforePath.get(), listener);
    }

}
