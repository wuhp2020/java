package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ListService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 增
     * @param key
     * @param value
     * @throws Exception
     */
    public void save(String key, String value) throws Exception {
        redisTemplate.opsForList().rightPush(key, value);
        List<String> ss = redisTemplate.opsForList().range(key, 0, -1);
        log.info(ss.get(0));
    }

    /**
     * 删
     * @param key
     * @return
     */
    @Transactional
    public Pair<Boolean, Object> delete(String key) {

        return Pair.of(true, redisTemplate.opsForList().leftPop(key));
    }

    /**
     * 查询
     * @param key
     * @return
     * @throws Exception
     */
    public List<String> findAll(String key) throws Exception{
        List<String> list = (List<String>) redisTemplate.opsForList().range(key, 0, -1);
        return list;
    }
}
