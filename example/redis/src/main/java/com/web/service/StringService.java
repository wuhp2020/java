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
public class StringService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 增
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public void save(String key, String value) throws Exception{
        try {
            Integer.valueOf(value);
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.opsForValue().increment(key, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删
     * @param keys
     * @return
     */
    @Transactional
    public Pair<Boolean, String> delete(List<String> keys) {
        keys.stream().forEach(key -> redisTemplate.delete(key));
        return Pair.of(true, "OK");
    }

    /**
     * 单个查询
     * @param key
     * @return
     * @throws Exception
     */
    public String findOne(String key) throws Exception{
        String json = (String) redisTemplate.opsForValue().get(key);
        return json;
    }

    public String increment(String key) throws Exception{
        Long i = redisTemplate.opsForValue().increment(key);
        return i.toString();
    }

    public String decrement(String key) throws Exception{
        return redisTemplate.opsForValue().decrement(key).toString();
    }

    public void topic(String message) throws Exception{
        redisTemplate.convertAndSend("topic", message);
    }

}
