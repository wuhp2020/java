package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class ThreadService {

    public void printABC() throws Exception {
        Thread t1 = new ABC("t1", 0, "A");
        Thread t2 = new ABC("t2", 1, "B");
        Thread t3 = new ABC("t3", 2, "C");

        t2.start();
        t1.start();
        t3.start();
    }

    public void createThreadPool() throws Exception {

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        final int[] b = {0};
        /**
         * corePoolSize: 提交一个任务，线程池里存活的核心线程数小于线程数corePoolSize时，线程池会创建一个核心线程去处理提交的任务
         * workQueue: 如果线程池核心线程数已满, 即线程数已经等于corePoolSize, 一个新提交的任务, 会被放进任务队列workQueue排队等待执行
         * maximumPoolSize: 当线程池里面存活的线程数已经等于corePoolSize了,
         * 并且任务队列workQueue也满, 判断线程数是否达到maximumPoolSize,
         * 即最大线程数是否已满, 如果没到达, 创建一个非核心线程执行提交的任务
         * keepAliveTime: 线程池中非核心线程空闲的存活时间大小
         * unit: 线程空闲存活时间单位
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5,
                60000, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>());
        Future feature = threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                // 线程封闭的, 也是线程安全的
                int a = 5;
                // 线程逃逸, 线程不安全
                b[0] = 10;
                a = 10;
                log.info(a + "");
                log.info("start thread...");
            }
        });

        feature.get();
    }

    public void threadLocal() throws Exception {
        // 为每个线程保存了自己的独立副本
        ThreadLocal threadLocal = new ThreadLocal();

        new Thread(() -> {
            threadLocal.set(2);
            Object aa = threadLocal.get();
            log.info(aa.toString());
        }).start();

        new Thread(() -> {
            threadLocal.set(1);
            Object bb = threadLocal.get();
            log.info(bb + "");
        }).start();
    }

    /**
     * 1.Runnable状态（正在运行或者等待调度的线程）可以用interrupt（）中断
     * 2.Block状态（等待锁的线程）不可以用interrupt（）中断
     * @throws Exception
     */
    public void join() throws Exception {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    log.info(Thread.currentThread().getName() + " -> " +i);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    log.info(Thread.currentThread().getName() + " -> " +i);
                }
            }
        });

        t1.setName("t1");
        t2.setName("t2");

        t1.start();
        t1.join(); // 主线程阻塞
        t2.start();
    }
}

@Slf4j
class ABC extends Thread {

    public static int count;
    private String name;
    private int flag;
    private String value;
    private static final Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public ABC(String name, int flag, String value) {
        this.name = name;
        this.flag = flag;
        this.value = value;
    }

    @Override
    public void run() {
        for (int i = 0;i < 30;i++) {
            lock.lock();
            while (count % 3 != flag) {
                try {
                    // 会释放锁
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            log.info(name +" -> "+ value);
            condition.signalAll();
            lock.unlock();
        }
    }
}

