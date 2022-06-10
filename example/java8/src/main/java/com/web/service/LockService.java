package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

@Service
@Slf4j
public class LockService {

    public void reentrantReadWriteLock() throws Exception {
        /**
         * ReentrantReadWriteLock是Lock的另一种实现方式, 我们已经知道了ReentrantLock是一个排他锁,
         * 同一时间只允许一个线程访问, 而ReentrantReadWriteLock允许多个读线程同时访问,
         * 但不允许写线程和读线程、写线程和写线程同时访问. 相对于排他锁, 提高了并发性. 在实际应用中,
         * 大部分情况下对共享数据（如缓存）的访问都是读操作远多于写操作,
         * 这时ReentrantReadWriteLock能够提供比排他锁更好的并发性和吞吐量.
         * 读写锁内部维护了两个锁, 一个用于读操作, 一个用于写操作. 所有 ReadWriteLock实现都必须保证
         * writeLock操作的内存同步效果也要保持与相关 readLock的联系.
         * 也就是说, 成功获取读锁的线程会看到写入锁之前版本所做的所有更新.
         *
         * ReentrantReadWriteLock支持以下功能:
         * 1）支持公平和非公平的获取锁的方式
         * 2）支持可重入. 读线程在获取了读锁后还可以获取读锁, 写线程在获取了写锁之后既可以再次获取写锁又可以获取读锁,
         * 3）还允许从写入锁降级为读取锁, 其实现方式是: 先获取写入锁, 然后获取读取锁,
         * 最后释放写入锁. 但是, 从读取锁升级到写入锁是不允许的,
         * 4）读取锁和写入锁都支持锁获取期间的中断
         * 5）Condition支持, 仅写入锁提供了一个 Conditon 实现
         * 读取锁不支持 Conditon, readLock().newCondition() 会抛出 UnsupportedOperationException。
         */
        // 读锁
        Lock readLock = new ReentrantReadWriteLock().readLock();
        readLock.lock();
        readLock.unlock();

        // 写锁
        Lock writeLock = new ReentrantReadWriteLock().writeLock();
        writeLock.lock();
        writeLock.unlock();
    }

    public void reentrantLock() throws Exception {
        // 非公平可重入的独占锁
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock(false);

        // 公平可重入的独占锁
        ReentrantLock lock3 = new ReentrantLock(true);
        lock3.lock();
        lock3.unlock();
    }

    /**
     * LongAdder 内部维护了 base 变量和 Cell[] 数组,
     * 当多线程并发写的情况下，各个线程都在写入自己的 Cell 中,
     * LongAdder 操作后返回的是个近似准确的值, 最终也会返回一个准确的值
     * @throws Exception
     */
    public void longAdder() throws Exception {
        LongAdder longAdder = new LongAdder();
        IntStream.range(0, 10).forEach(i -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    IntStream.range(0, 20).forEach(j -> {
                        longAdder.increment();
                    });
                }
            }).start();
        });

        log.info(longAdder.longValue() + "");
    }

    /**
     * 一个同步辅助类, 在完成一组正在其他线程中执行的操作之前, 它允许一个或多个线程一直等待.
     * 用给定的计数 初始化 CountDownLatch. 由于调用了 countDown() 方法, 所以在当前计数到达零之前,
     * await 方法会一直受阻塞. 之后, 会释放所有等待的线程, await 的所有后续调用都将立即返回.
     * 这种现象只出现一次——计数无法被重置. 一个线程(或者多个), 等待另外N个线程完成某个事情之后才能执行
     * @throws Exception
     */
    public void countDownLatch() throws Exception {
        int threadNum = 20;
        AtomicInteger integer = new AtomicInteger(0);
        CountDownLatch end = new CountDownLatch(threadNum);

        for(int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                try {
                    log.info(integer.incrementAndGet() + "->");
                } catch (Exception e) {
                    log.error("error", e);
                } finally {
                    end.countDown();
                }
            }).start();
        }
        Thread.sleep(1000);
        end.await();
        log.info(integer.get() + "<-");
    }

    /**
     * CyclicBarrier 的字面意思是可循环使用（Cyclic）的屏障（Barrier）.
     * 它要做的事情是, 让一组线程到达一个屏障（也可以叫同步点）时被阻塞,
     * 直到最后一个线程到达屏障时, 屏障才会开门, 所有被屏障拦截的线程才会继续干活.
     * CyclicBarrier默认的构造方法是CyclicBarrier(int parties), 其参数表示屏障拦截的线程数量,
     * 每个线程调用await方法告诉CyclicBarrier我已经到达了屏障, 然后当前线程被阻塞
     * @throws Exception
     */
    public void cyclicBarrier() throws Exception {
        int threadNum = 20;
        CyclicBarrier start = new CyclicBarrier(threadNum);

        for(int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                try {
                    start.await();
                } catch (Exception e) {
                    log.error("error", e);
                }
                log.info(Thread.currentThread().getName() + ", " + System.currentTimeMillis() + ", start!");
            }).start();
        }
    }

    private static int num = 0;
    public void semaphore() throws Exception {
        Semaphore sp = new Semaphore(1);
        for(int i = 0; i < 30; i++) {
            new Thread(() -> {
                try {
                    sp.acquire();
                    log.info(num++ + "->");
                } catch (Exception e) {
                    log.error("error", e);
                } finally {
                    sp.release();
                }
            }).start();
        }
    }

}
