package com.web.service;

import com.web.vo.lock.LockVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LockService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void lock(LockVO lockVO) throws Exception {
        boolean result = redisTemplate.opsForValue().setIfAbsent(lockVO.getKey(), 1, 10, TimeUnit.SECONDS);
        if(!result) {
            throw new Exception("获取锁失败");
        }
    }

    public void unlock(LockVO lockVO) {
        redisTemplate.delete(lockVO.getKey());
    }

}
