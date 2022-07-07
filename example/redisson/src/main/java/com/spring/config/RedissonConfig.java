package com.spring.config;

import com.google.common.collect.Lists;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class RedissonConfig {

    @Value("${redisson.nodes}")
    private String nodes;

    @Value("${redisson.cluster.enable}")
    private boolean isCluster;

    @Bean(destroyMethod="shutdown")
    RedissonClient redisson() throws IOException {
        //1、创建配置
        Config config = new Config();
        config.setLockWatchdogTimeout(30 * 1000);
        if (isCluster) {
            List<String> list = Lists.newArrayList();
            for (String s : nodes.split(",")) {
                list.add("redis://" + s);
            }
            config.useClusterServers().addNodeAddress(list.toArray(new String[list.size()]));
        } else {
            config.useSingleServer().setAddress("redis://" + nodes).setDatabase(0);
        }
        return Redisson.create(config);
    }
}
