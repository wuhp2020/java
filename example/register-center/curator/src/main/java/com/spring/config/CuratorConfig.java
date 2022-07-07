package com.spring.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/18
 * @ Desc   : 描述
 */
@Configuration
public class CuratorConfig {

    @Value("${zookeeper.nodes}")
    private String nodes;

    @Bean
    CuratorFramework client() throws IOException {
        //1、创建配置
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(nodes).connectionTimeoutMs(15 * 1000)
                .sessionTimeoutMs(60 * 100).retryPolicy(retryPolicy).build();
        client.start();
        return client;
    }
}
