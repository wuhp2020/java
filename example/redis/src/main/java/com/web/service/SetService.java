package com.web.service;

import com.web.vo.set.SetSaveVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SetService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 增
     * @param setSaveVO
     * @throws Exception
     */
    public void save(SetSaveVO setSaveVO) throws Exception{
        redisTemplate.opsForSet().add(setSaveVO.getKey(), setSaveVO.getValue());
    }

    /**
     * 删
     * @param key
     * @return
     */
    @Transactional
    public Pair<Boolean, Object> delete(String key) {
        return Pair.of(true, redisTemplate.opsForSet().pop(key));
    }

    /**
     * 单个查询
     * @param key
     * @return
     * @throws Exception
     */
    public String findOne(String key) throws Exception{
        String json = (String) redisTemplate.opsForSet().pop(key);
        return json;
    }

    /**
     * 并集
     * @param key1
     * @param key2
     * @return
     * @throws Exception
     */
    public Object union(String key1, String key2) throws Exception{
        Object value = redisTemplate.opsForSet().union(key1, key1);
        return value;
    }

    /**
     * 交集
     * @param key1
     * @param key2
     * @return
     * @throws Exception
     */
    public Object inter(String key1, String key2) throws Exception{
        Object value = redisTemplate.opsForSet().intersect(key1, key1);
        return value;
    }

    /**
     * 差集
     * @param key1
     * @param key2
     * @return
     * @throws Exception
     */
    public Object diff(String key1, String key2) throws Exception{
        Object value = redisTemplate.opsForSet().difference(key1, key1);
        return value;
    }
}
