package com;

import com.spring.handler.WebSocketChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class NettyWebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyWebSocketApplication.class, args);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new WebSocketChannelHandler());
            log.info("服务端开启等待客户端连接...");

            Channel channel = bootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("服务端启动失败", e);
        } finally {
            // 退出程序
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            log.info("服务端已关闭");
        }
    }
}
