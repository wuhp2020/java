package com.spring.config;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.context.annotation.Configuration;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/17
 * @ Desc   : 描述
 */
@Configuration
public class NettyConfig {
    /**
     * 存储每一个客户端接入进来时的channel对象
     */
    public final static ChannelGroup GROUP =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
