package com.web.controller;

import com.web.service.IOService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/nio")
@Api(tags = "NIO流")
@Slf4j
public class IOController {

    @Autowired
    private IOService ioService;

    @ApiOperation(value = "分配一个指定大小的缓冲区")
    @PostMapping("bytebuffer/create")
    public void createByteBuffer() {
        ioService.createByteBuffer();
    }

    @ApiOperation(value = "利用put存入数据到缓冲区中")
    @PostMapping("bytebuffer/put")
    public void putByteBuffer() {
        ioService.putByteBuffer();
    }

    @ApiOperation(value = "切换到读数据模式")
    @GetMapping("bytebuffer/flip")
    public void flipByteBuffer() {
        ioService.flipByteBuffer();
    }

    @ApiOperation(value = "利用get读取缓冲区中的数据")
    @GetMapping("bytebuffer/get")
    public void getByteBuffer() {
        ioService.getByteBuffer();
    }

    @ApiOperation(value = "rewind可重复读")
    @PostMapping("bytebuffer/rewind")
    public void rewindByteBuffer() {
        ioService.rewindByteBuffer();
    }

    @ApiOperation(value = "clear清空缓冲区")
    @DeleteMapping("bytebuffer/clear")
    public void clearByteBuffer() {
        ioService.clearByteBuffer();
    }

    @ApiOperation(value = "使用直接缓冲区完成文件的复制(内存映射文件)")
    @PostMapping("allocatedirect")
    public void allocateDirect() throws Exception {
        ioService.allocateDirect();
    }

    @ApiOperation(value = "利用通道完成文件的复制(非直接缓冲区)")
    @PostMapping("allocate")
    public void allocate() throws Exception {
        ioService.allocate();
    }

    @ApiOperation(value = "创建通道")
    @PostMapping("channel/create")
    public void createChannel() throws Exception {
        ioService.createChannel();
    }

    @ApiOperation(value = "分散读取")
    @GetMapping("scatter/reads")
    public void scatterReads() throws Exception {
        ioService.scatterReads();
    }

    @ApiOperation(value = "聚集写入")
    @PostMapping("gather/writes")
    public void gatherWrites() throws Exception {
        ioService.gatherWrites();
    }

}
