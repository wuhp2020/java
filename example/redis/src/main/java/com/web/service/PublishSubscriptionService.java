package com.web.service;

import com.spring.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PublishSubscriptionService {

    @Autowired
    private RedisTemplate<String, String> redisAlertTemplate;

    public void alert(String message) {
        redisAlertTemplate.convertAndSend(RedisConfig.ALERT_TOPIC, message);
    }
}


