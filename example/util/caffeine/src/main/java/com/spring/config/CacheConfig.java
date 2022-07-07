package com.spring.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @ Author : wuheping
 * @ Date   : 2022/3/8
 * @ Desc   : 描述
 */
@Configuration
public class CacheConfig {

    @Bean("caffeineCacheManager")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
//                .expireAfterAccess(3600, TimeUnit.SECONDS)
                // 设置最后一次写入后经过固定时间过期
                .expireAfterWrite(3600, TimeUnit.SECONDS)
                // 创建缓存或者最近更新缓存后经过固定时间刷新缓存
//                .refreshAfterWrite(3600, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(10000));
        return cacheManager;
    }
}
