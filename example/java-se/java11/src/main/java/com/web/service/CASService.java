package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.stream.IntStream;

/**
 * 原子化的基本数据类型
 * 原子化的对象引用类型
 * 原子化数组
 * 原子化对象属性更新器
 * 原子化的累加器
 */

@Service
@Slf4j
public class CASService {

    /**
     * 原子化的对象引用类型
     */
    private static AtomicStampedReference<Integer> count = new AtomicStampedReference<>(10, 0);

    /**
     * 对于AtomicReference可能出现对象属性丢失的情况,
     * 即oldObject == current, 但是oldObject.getPropertyA != current.getPropertyA
     * 这时候, AtomicStampedReference就派上用场了, 即加上版本号
     */
    public void atomicStampedReference() {
        Thread main = new Thread(() -> {
            int stamp = count.getStamp(); //获取当前版本

            log.info("线程{} 当前版本{}",Thread.currentThread(),stamp);
            try {
                Thread.sleep(1000); //等待1秒 ，以便让干扰线程执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isCASSuccess = count.compareAndSet(10, 12, stamp, stamp + 1);  //此时expectedReference未发生改变，但是stamp已经被修改了,所以CAS失败
            log.info("CAS是否成功={}",isCASSuccess);
        }, "主操作线程");

        Thread other = new Thread(() -> {
            int stamp = count.getStamp(); //获取当前版本
            log.info("线程{} 当前版本{}",Thread.currentThread(),stamp);
            count.compareAndSet(10, 12, stamp, stamp + 1);
            log.info("线程{} 增加后版本{}",Thread.currentThread(),count.getStamp());

            // 模拟ABA问题 先更新成12 又更新回10
            int stamp1 = count.getStamp(); //获取当前版本
            count.compareAndSet(12, 10, stamp1, stamp1 + 1);
            log.info("线程{} 减少后版本{}",Thread.currentThread(),count.getStamp());
        }, "干扰线程");

        main.start();
        other.start();
    }

    public void atomicInteger() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();

        IntStream.range(0, 10).forEach(i -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    atomicInteger.incrementAndGet();
                }
            }).start();
        });

        log.info(atomicInteger.get() + "");
    }
}
