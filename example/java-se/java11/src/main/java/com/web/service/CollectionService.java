package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CollectionService {

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    /**
     * corePoolSize: 提交一个任务，线程池里存活的核心线程数小于线程数corePoolSize时，线程池会创建一个核心线程去处理提交的任务
     * workQueue: 如果线程池核心线程数已满, 即线程数已经等于corePoolSize, 一个新提交的任务, 会被放进任务队列workQueue排队等待执行
     * maximumPoolSize: 当线程池里面存活的线程数已经等于corePoolSize了, 并且任务队列workQueue也满, 判断线程数是否达到maximumPoolSize, 即最大线程数是否已满, 如果没到达, 创建一个非核心线程执行提交的任务
     * keepAliveTime: 线程池中非核心线程空闲的存活时间大小
     * unit: 线程空闲存活时间单位
     */
    private ExecutorService threadPoolExecutor = new ThreadPoolExecutor(5, 5, 60000, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>());

    public void concurrentHashMap() throws Exception {

        ConcurrentHashMap<String, String> cMap = new ConcurrentHashMap<String, String>();

        // 同步工具
        CountDownLatch count = new CountDownLatch(2);
        fixedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                for(int j = 0; j < 5; ++j) {
                    cMap.put(j + "", j + "");
                }
                count.countDown();
            }
        });
        fixedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                for (int j = 5; j < 10; j++) {
                    cMap.put(j + "", j + "");
                }
                count.countDown();
            }
        });
        count.await();
        log.info(cMap.toString());
    }

    public void hashMap() throws Exception {
        // 默认初始化大小16, 在第一次put()时初始化
        HashMap<String, String> map = new HashMap<>(1);

        /**
         * HashMap之put方法图解
         */
        map.put("name", "wuhp");

        /**
         * HashMap之resize方法图解
         */

        /**
         * HashMap之get方法图解
         */
        log.info(map.get("name"));
        map.remove("name");
    }

    public void quickSort(List<Integer> list, int left, int right) {
        // 6, 3, 8, 4, 7, 2, 1, 5, 0, 9
        // 6 基准
        // 6, 3, 8, 4, 7, 2, 1, 5, 0, 9 // 左移找0
        // 6, 3, 8, 4, 7, 2, 1, 5, 0, 9 // 右移找8
        // 6, 3, 0, 4, 7, 2, 1, 5, 8, 9 // 0 8交换
        // 6, 3, 0, 4, 7, 2, 1, 5, 8, 9 // 左移找5
        // 6, 3, 0, 4, 7, 2, 1, 5, 8, 9 // 右移找7
        // 6, 3, 0, 4, 5, 2, 1, 7, 8, 9 // 5 7交换
        // 6, 3, 0, 4, 5, 2, 1, 7, 8, 9 // 左移找1
        // 6, 3, 0, 4, 5, 2, 1, 7, 8, 9 // 右移找1

        // 如果left等于right, 即数组只有一个元素, 直接返回
        if (left >= right) {
            return;
        }

        // 设置最左边的元素为基准值
        int key = list.get(left);

        // 数组中比key小的放在左边, 比key大的放在右边, key值下标为i
        int i = left;
        int j = right;
        while (i < j) {
            // j向左移, 直到遇到比key小的值
            while (list.get(j) >= key && i < j) {
                j--;
            }

            // i向右移, 直到遇到比key大的值
            while (list.get(i) <= key && i < j) {
                i++;
            }

            // i和j指向的元素交换
            if (i < j) {
                int temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        list.set(left, list.get(i));
        list.set(i, key);

        quickSort(list, left, i - 1);
        quickSort(list, i + 1, right);
    }
}
