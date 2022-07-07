package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeysService {

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public boolean move(String key, int index) {
        return redisTemplate.move(key, index);
    }

    public DataType type(String key) {
        return redisTemplate.type(key);
    }
}
