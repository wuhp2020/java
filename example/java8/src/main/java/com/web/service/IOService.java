package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@Slf4j
public class IOService {

    public ByteBuffer createByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        int capacity = byteBuffer.capacity();
        int limit = byteBuffer.limit();
        int position = byteBuffer.position();
        log.info("容量: ", capacity);
        log.info("可以操作数据的大小: ", limit);
        log.info("当前位置: ", position);

        return byteBuffer;
    }

    public ByteBuffer putByteBuffer() {
        ByteBuffer byteBuffer = this.createByteBuffer();

        byteBuffer.put("abcde".getBytes());

        int capacity = byteBuffer.capacity();
        int limit = byteBuffer.limit();
        int position = byteBuffer.position();
        log.info("容量: ", capacity);
        log.info("可以操作数据的大小: ", limit);
        log.info("当前位置: ", position);

        return byteBuffer;
    }

    public ByteBuffer flipByteBuffer() {

        ByteBuffer byteBuffer = this.putByteBuffer();
        byteBuffer.flip();
        int capacity = byteBuffer.capacity();
        int limit = byteBuffer.limit();
        int position = byteBuffer.position();
        log.info("容量: ", capacity);
        log.info("可以操作数据的大小: ", limit);
        log.info("当前位置: ", position);

        return byteBuffer;

    }

    public ByteBuffer getByteBuffer() {
        ByteBuffer byteBuffer = this.flipByteBuffer();
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst);
        int capacity = byteBuffer.capacity();
        int limit = byteBuffer.limit();
        int position = byteBuffer.position();
        log.info("容量: ", capacity);
        log.info("可以操作数据的大小: ", limit);
        log.info("当前位置: ", position);

        return byteBuffer;
    }

    public ByteBuffer rewindByteBuffer() {
        ByteBuffer byteBuffer = this.getByteBuffer();
        int capacity = byteBuffer.capacity();
        int limit = byteBuffer.limit();
        int position = byteBuffer.position();
        log.info("容量: ", capacity);
        log.info("可以操作数据的大小: ", limit);
        log.info("当前位置: ", position);

        return byteBuffer;
    }

    public ByteBuffer clearByteBuffer() {
        ByteBuffer byteBuffer = this.rewindByteBuffer();
        byteBuffer.clear();

        int capacity = byteBuffer.capacity();
        int limit = byteBuffer.limit();
        int position = byteBuffer.position();
        log.info("容量: ", capacity);
        log.info("可以操作数据的大小: ", limit);
        log.info("当前位置: ", position);

        return byteBuffer;
    }

    public ByteBuffer allocateDirect() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

        log.info("判断是直接缓冲区还是非直接缓冲区: ", byteBuffer.isDirect());

        return byteBuffer;
    }

    public ByteBuffer allocate() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        log.info("判断是直接缓冲区还是非直接缓冲区: ", byteBuffer.isDirect());

        return byteBuffer;
    }

    public ByteBuffer createChannel() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        FileInputStream fis = new FileInputStream("");
        FileChannel fileChannel = fis.getChannel();
        fileChannel.read(byteBuffer);


        FileChannel inChannel = FileChannel.open(Paths.get("a.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("d.txt"),
                StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        inChannel.transferTo(0, inChannel.size(), outChannel);
        // 或者可以使用下面这种方式
        //outChannel.transferFrom(inChannel, 0, inChannel.size());
        inChannel.close();
        outChannel.close();
        return byteBuffer;
    }

    public void scatterReads() throws Exception {

        RandomAccessFile raf = new RandomAccessFile("a.txt", "rw");
        // 获取通道
        FileChannel channel = raf.getChannel();
        // 分配指定大小缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(2);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);
        // 分散读取
        ByteBuffer[] bufs = {buf1, buf2};
        channel.read(bufs);  // 参数需要一个数组
        for (ByteBuffer byteBuffer : bufs) {
            byteBuffer.flip();  // 切换到读模式
        }
        log.info(new String(bufs[0].array(), 0, bufs[0].limit()));
        log.info(new String(bufs[1].array(), 0, bufs[1].limit()));
    }

    public void gatherWrites() throws Exception {

        RandomAccessFile raf = new RandomAccessFile("a.txt", "rw");
        // 获取通道
        FileChannel channel = raf.getChannel();
        // 分配指定大小缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(2);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);
        // 分散读取
        ByteBuffer[] bufs = {buf1, buf2};
        channel.read(bufs);  // 参数需要一个数组
        for (ByteBuffer byteBuffer : bufs) {
            byteBuffer.flip();  // 切换到读模式
        }
        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

        // 聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("e.txt","rw");
        // 获取通道
        FileChannel channel2 = raf2.getChannel();
        // 把 bufs 里面的几个缓冲区聚集到 channel2 这个通道中, 聚集到通道中, 也就是到了 e.txt 文件中
        channel2.write(bufs);
        channel2.close();
    }
}
