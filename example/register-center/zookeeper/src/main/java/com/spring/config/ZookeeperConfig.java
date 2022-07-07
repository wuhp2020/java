package com.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ZookeeperConfig {

    @Value("${zookeeper.address}")
    private String address;

    @Bean(name = "zkClient")
    public ZkClient zkClient() {
        return new ZkClient(address);
    }
}
