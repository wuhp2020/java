package com.web.controller;

import com.Java8Application;
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
    public void atomicStampedReference() {
        casService.atomicStampedReference();
    }


    public volatile int index = 0;
    @ApiOperation(value = "unsafe测试")
    @GetMapping("unsafe")
    public void unsafe() throws Exception {
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
                        if (unsafe.compareAndSwapInt(Java8Application.applicationContext()
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
    }
}
