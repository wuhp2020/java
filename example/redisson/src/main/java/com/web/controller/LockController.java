package com.web.controller;

import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/lock")
@Api(tags = "锁")
@Slf4j
public class LockController {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @PostMapping("reentrantLock")
    @ApiOperation(value = "可重入锁")
    public ResponseVO reentrantLock() {
        RLock rLock = redissonClient.getLock("reentrantLock");
        try {
            rLock.lock(10, TimeUnit.SECONDS);
            log.info("执行开始");
            Thread.sleep(50000);
            log.info("执行结束");

            return ResponseVO.SUCCESS("获取锁成功");
        } catch (Exception e) {
            log.error("reentrantLock 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        } finally {
            rLock.unlock();
        }
    }

    @PostMapping("continueLock")
    @ApiOperation(value = "续命锁")
    public ResponseVO continueLock() {
        RLock rLock = redissonClient.getLock("continueLock");
        try {
            rLock.lock();
            log.info("执行开始==");
            Thread.sleep(40000);
            log.info("执行结束==");
            return ResponseVO.SUCCESS("获取锁成功==");
        } catch (Exception e) {
            log.error("continueLock 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        } finally {
            rLock.unlock();
        }
    }

    @PostMapping("continueLockException")
    @ApiOperation(value = "续命锁异常")
    public ResponseVO continueLockException() {
        RLock rLock = redissonClient.getLock("continueLock");
        try {
            rLock.lock();
            log.info("执行开始Exception==");
            throw new RuntimeException("测试分布式锁释放");
        } catch (Exception e) {
            log.error("continueLock 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        } finally {
            rLock.unlock();
        }
    }

    @PostMapping("close")
    @ApiOperation(value = "续命模拟锁宕机")
    public ResponseVO close() {
        RLock rLock = redissonClient.getLock("continueLock");
        rLock.lock();
        log.info("执行开始close==");
        applicationContext.close();
        return ResponseVO.SUCCESS("ok");
    }

    @PostMapping("multiLock")
    @ApiOperation(value = "分布式MultiLock")
    public ResponseVO multiLock() {
        RLock lock1 = redissonClient.getLock("lock1");
        RLock lock2 = redissonClient.getLock("lock2");
        RLock lock3 = redissonClient.getLock("lock3");
        RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
        lock.lock();
        return ResponseVO.SUCCESS("ok");
    }

    @PostMapping("readWriteLock")
    @ApiOperation(value = "分布式ReadWriteLock")
    public ResponseVO readWriteLock() throws Exception {
        RReadWriteLock rwlock = redissonClient.getReadWriteLock("anyRWLock");
        rwlock.readLock().lock();
        rwlock.writeLock().lock();
        rwlock.readLock().lock(10, TimeUnit.SECONDS);
        rwlock.writeLock().lock(10, TimeUnit.SECONDS);
        boolean res1 = rwlock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
        boolean res2 = rwlock.writeLock().tryLock(100, 10, TimeUnit.SECONDS);
        rwlock.readLock().unlock();
        rwlock.writeLock().unlock();
        return ResponseVO.SUCCESS("ok");
    }

    @PostMapping("semaphore")
    @ApiOperation(value = "分布式Semaphore")
    public ResponseVO semaphore() throws Exception {
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        semaphore.acquire();
        semaphore.acquire(23);
        semaphore.tryAcquire();
        semaphore.tryAcquire(23, TimeUnit.SECONDS);
        semaphore.release(10);
        semaphore.release();
        return ResponseVO.SUCCESS("ok");
    }

    @PostMapping("countDownLatch")
    @ApiOperation(value = "分布式CountDownLatch")
    public ResponseVO countDownLatch() throws Exception {
        RCountDownLatch latch1 = redissonClient.getCountDownLatch("anyCountDownLatch");
        latch1.trySetCount(1);
        latch1.await();

        // in other thread or other JVM
        RCountDownLatch latch2 = redissonClient.getCountDownLatch("anyCountDownLatch");
        latch2.countDown();
        return ResponseVO.SUCCESS("ok");
    }
}
