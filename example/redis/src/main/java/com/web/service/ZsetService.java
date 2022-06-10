package com.web.service;

import com.google.gson.Gson;
import com.web.vo.zset.ZsetSaveVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Slf4j
public class ZsetService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 增
     * @param zsetSaveVO
     * @throws Exception
     */
    public void save(ZsetSaveVO zsetSaveVO) throws Exception{
        redisTemplate.opsForZSet().add(zsetSaveVO.getKey(), zsetSaveVO.getValue(), zsetSaveVO.getScore());
    }

    /**
     * 删
     * @param key
     * @return
     */
    @Transactional
    public Pair<Boolean, String> delete(String key) {
        redisTemplate.opsForZSet().remove(key);
        return Pair.of(true, "OK");
    }

    /**
     * 单个查询
     * @param key
     * @return
     * @throws Exception
     */
    public String find(String key) throws Exception{
        Set<Object> set = redisTemplate.opsForZSet().range(key, 0, -1);
        return new Gson().toJson(set);
    }
}
