package com.web.controller;

import com.Java11Application;
import com.web.service.CASService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/api/v1/cas")
@Api(tags = "原子操作")
@Slf4j
public class CASController {

    private CASService casService;

    @ApiOperation(value = "atomicStampedReference测试")
    @PostMapping("atomicStampedReference")
    public ResponseVO atomicStampedReference() {
        try {
            casService.atomicStampedReference();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:atomicStampedReference() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }


    public volatile int index = 0;
    @ApiOperation(value = "unsafe测试")
    @GetMapping("unsafe")
    public ResponseVO unsafe() {
        try {
            CountDownLatch latch = new CountDownLatch(30);

            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe)theUnsafeField.get(null);
            long offset = unsafe.objectFieldOffset(CASController.class.getField("index"));

            for (int i = 0; i < 30; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            int temp = index + 1;
                            if (unsafe.compareAndSwapInt(Java11Application.applicationContext()
                                    .getBean("CASController"), offset, index, temp)) {
                                latch.countDown();
                                break;
                            }
                        }
                    }
                }).start();
            }
            latch.await();

            log.info(index + " ==========");

            return ResponseVO.SUCCESS(index);
        } catch (Exception e) {
            log.error("method:cas() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
