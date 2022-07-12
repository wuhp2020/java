package com.web.controller;

import com.web.service.LockService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lock")
@Api(tags = "锁")
@Slf4j
public class LockController {

    /**
     * AQS定义两种资源共享方式: Exclusive（独占, 只有一个线程能执行, 如ReentrantLock）
     * 和Share（共享, 多个线程可同时执行, 如Semaphore/CountDownLatch）
     *
     * 重要的方法:
     * 不同的自定义同步器争用共享资源的方式也不同. 自定义同步器在实现时只需要实现共享资源state的获取与释放方式即可,
     * 至于具体线程等待队列的维护（如获取资源失败入队/唤醒出队等）, AQS已经在顶层实现好了.
     * 自定义同步器实现时主要实现以下几种方法:
     * isHeldExclusively(): 该线程是否正在独占资源, 只有用到condition才需要去实现它.
     * tryAcquire(int): 独占方式, 尝试获取资源, 成功则返回true, 失败则返回false.
     * tryRelease(int): 独占方式, 尝试释放资源, 成功则返回true, 失败则返回false.
     * tryAcquireShared(int): 共享方式, 尝试获取资源, 负数表示失败; 0表示成功, 但没有剩余可用资源; 正数表示成功, 且有剩余资源.
     * tryReleaseShared(int): 共享方式, 尝试释放资源, 如果释放后允许唤醒后续等待结点返回true, 否则返回false.
     * 案例:
     * 以ReentrantLock为例: state初始化为0, 表示未锁定状态. A线程lock()时, 会调用tryAcquire()独占该锁并将state+1.
     * 此后, 其他线程再tryAcquire()时就会失败, 直到A线程unlock()到state=0（即释放锁）为止, 其它线程才有机会获取该锁.
     * 当然, 释放锁之前, A线程自己是可以重复获取此锁的（state会累加）, 这就是可重入的概念.
     * 但要注意, 获取多少次就要释放多么次, 这样才能保证state是能回到零态的.
     *
     * 再以CountDownLatch以例: 任务分为N个子线程去执行, state也初始化为N（注意N要与线程个数一致）.
     * 这N个子线程是并行执行的, 每个子线程执行完后countDown()一次, state会CAS减1.
     * 等到所有子线程都执行完后(即state=0), 会unpark()主调用线程, 然后主调用线程就会从await()函数返回, 继续后余动作.
     *
     * AQS参数详解:
     * 结点状态waitStatus:
     * 这里我们说下Node, Node结点是对每一个等待获取资源的线程的封装,
     * 其包含了需要同步的线程本身及其等待状态, 如是否被阻塞、是否等待唤醒、是否已经被取消等.
     * 变量waitStatus则表示当前Node结点的等待状态, 共有5种取值:
     * CANCELLED(1): 表示当前结点已取消调度, 当timeout或被中断（响应中断的情况下）, 会触发变更为此状态, 进入该状态后的结点将不会再变化.
     * SIGNAL(-1): 表示后继结点在等待当前结点唤醒, 后继结点入队时, 会将前继结点的状态更新为SIGNAL.
     * CONDITION(-2): 表示结点等待在Condition上, 当其他线程调用了Condition的signal()方法后, CONDITION状态的结点将从等待队列转移到同步队列中, 等待获取同步锁.
     * PROPAGATE(-3): 共享模式下, 前继结点不仅会唤醒其后继结点, 同时也可能会唤醒后继的后继结点.
     * 0: 新结点入队时的默认状态.
     *
     * AQS流程:
     * acquire(int):
     * 调用自定义同步器的tryAcquire()尝试直接去获取资源, 如果成功则直接返回.
     * 没成功, 则addWaiter()将该线程加入等待队列的尾部, 并标记为独占模式.
     * acquireQueued()使线程在等待队列中休息, 有机会时（轮到自己, 会被unpark()）会去尝试获取资源.
     * 获取到资源后才返回, 如果在整个等待过程中被中断过, 则返回true, 否则返回false.
     * 如果线程在等待过程中被中断过, 它是不响应的, 只是获取资源后才再进行自我中断selfInterrupt(), 将中断补上.
     *
     * release(int):
     * 它是根据tryRelease()的返回值来判断该线程是否已经完成释放掉资源了, 所以自定义同步器在设计tryRelease()的时候要明确这一点.
     *
     * acquireShared(int):
     * tryAcquireShared()尝试获取资源, 成功则直接返回.
     * 失败则通过doAcquireShared()进入等待队列park(), 直到被unpark()/interrupt()并成功获取到资源才返回, 整个等待过程也是忽略中断的.
     * 跟独占模式比, 还有一点需要注意的是, 这里只有线程是head.next时（“老二”）, 才会去尝试获取资源,
     * 有剩余的话还会唤醒之后的队友, 那么问题就来了, 假如老大用完后释放了5个资源, 而老二需要6个, 老三需要1个, 老四需要2个.
     * 老大先唤醒老二, 老二一看资源不够, 他是把资源让给老三呢, 还是不让? 答案是否定的.
     * 老二会继续park()等待其他线程释放资源, 也更不会去唤醒老三和老四了. 独占模式, 同一时刻只有一个线程去执行, 这样做未尝不可;
     * 但共享模式下, 多个线程是可以同时执行的, 现在因为老二的资源需求量大, 而把后面量小的老三和老四也都卡住了.
     * 当然, 这并不是问题, 只是AQS保证严格按照入队顺序唤醒罢了（保证公平, 但降低了并发）.
     *
     * releaseShared():
     * 独占模式下的tryRelease()在完全释放掉资源（state=0）后, 才会返回true去唤醒其他线程, 这主要是基于独占下可重入的考量,
     * 而共享模式下的releaseShared()则没有这种要求, 共享模式实质就是控制一定量的线程并发执行,
     * 那么拥有资源的线程在释放掉部分资源时就可以唤醒后继等待结点.
     */

    @Autowired
    private LockService lockService;

    @ApiOperation(value = "ReentrantReadWriteLock")
    @PostMapping("reentrantReadWriteLock")
    public void reentrantReadWriteLock() throws Exception {
        lockService.reentrantReadWriteLock();
    }

    @ApiOperation(value = "reentrantLock")
    @PostMapping("reentrantLock")
    public void reentrantLock() throws Exception {
        lockService.reentrantLock();
    }

    @ApiOperation(value = "闭锁")
    @GetMapping("countDownLatch")
    public void countDownLatch() throws Exception {
        lockService.countDownLatch();
    }

    @ApiOperation(value = "栅栏")
    @GetMapping("cyclicBarrier")
    public void cyclicBarrier() throws Exception {
        lockService.cyclicBarrier();
    }

    @ApiOperation(value = "信号量")
    @GetMapping("semaphore")
    public void semaphore() throws Exception {
        lockService.semaphore();
    }
}
