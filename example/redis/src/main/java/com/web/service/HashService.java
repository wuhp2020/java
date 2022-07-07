package com.web.service;

import com.web.vo.hash.HashSaveVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class HashService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 增
     * @param hashSaveVO
     * @throws Exception
     */
    public void save(HashSaveVO hashSaveVO) throws Exception{
        redisTemplate.opsForHash().put(hashSaveVO.getId(), hashSaveVO.getKey(), hashSaveVO.getValue());
        redisTemplate.expire(hashSaveVO.getId(), 1, TimeUnit.HOURS);
    }

    /**
     * 删
     * @param id
     * @param key
     * @return
     */
    @Transactional
    public Pair<Boolean, String> delete(String id, String key) {
        Object hv = redisTemplate.opsForHash().get(id, key);
        redisTemplate.opsForHash().delete(id, hv);
        return Pair.of(true, "OK");
    }

    /**
     * 单个查询
     * @param key
     * @return
     * @throws Exception
     */
    public Object findOne(String id, String key) throws Exception{
        Object value =  redisTemplate.opsForHash().get(id, key);
        Map<Object, Object> keys = redisTemplate.opsForHash().entries(id);
        for (Object o: keys.entrySet()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) o;
            log.info(entry.getValue());
        }
        return value;
    }
}
