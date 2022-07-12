package com.web.controller;

import com.web.service.ReferenceService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

@RestController
@RequestMapping("/api/v1/reference")
@Api(tags = "引用类型")
@Slf4j
public class ReferenceController {

    @Autowired
    private ReferenceService referenceService;

    @ApiOperation(value = "强引用")
    @GetMapping("forceReference")
    public void forceReference() {
        // 强引用有引用变量指向时永远不会被垃圾回收, JVM宁愿抛出OutOfMemory错误也不会回收这种对象
        String a = new String("a");
        log.info(a);
    }

    @ApiOperation(value = "软引用")
    @GetMapping("softReference")
    public void softReference() {
        // 如果一个对象具有软引用, 内存空间足够, 垃圾回收器就不会回收它
        // 如果内存空间不足了, 就会回收这些对象的内存. 只要垃圾回收器没有回收它, 该对象就可以被程序使用.
        // 垃圾收集线程会在虚拟机抛出OutOfMemoryError之前回收软可及对象,
        // 而且虚拟机会尽可能优先回收长时间闲置不用的软可及对象,
        // 对那些刚刚构建的或刚刚使用过的“新”软可反对象会被虚拟机尽可能保留
        String a = new String("aaa");
        SoftReference reference = new SoftReference(a);
        a = null;
        log.info(reference.get() + "");
    }

    @ApiOperation(value = "弱引用")
    @GetMapping("weakReference")
    public void weakReference() {
        // 弱引用也是用来描述非必需对象的, 当JVM进行垃圾回收时, 无论内存是否充足, 都会回收被弱引用关联的对象
        String a = new String("a");
        WeakReference<String> reference = new WeakReference<String>(a);
        a = null;
        log.info(reference.get());
    }

    @ApiOperation(value = "虚引用")
    @GetMapping("phantomReference")
    public void phantomReference() {
        // 虚引用必须和引用队列关联使用, 当垃圾回收器准备回收一个对象时,
        // 如果发现它还有虚引用, 就会把这个虚引用加入到与之 关联的引用队列中.
        // 程序可以通过判断引用队列中是否已经加入了虚引用, 来了解被引用的对象是否将要被垃圾回收.
        // 如果程序发现某个虚引用已经被加入到引用队列, 那么就可以在所引用的对象的内存被回收之前采取必要的行动
        String a = new String("a");
        ReferenceQueue<String> queue = new ReferenceQueue<String>();
        PhantomReference<String> reference = new PhantomReference<String>(a, queue);
        a = null;
        log.info(reference.get());
    }
}
