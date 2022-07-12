package com.web.controller;

import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.PatternMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/21
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/topic")
@Api(tags = "消息")
@Slf4j
public class TopicController {

    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("topic")
    @ApiOperation(value = "Topic")
    public void topic() throws Exception {
        RTopic topic1 = redissonClient.getTopic("anyTopic");

        topic1.addListener(Object.class , new MessageListener<Object>() {

            @Override
            public void onMessage(CharSequence charSequence, Object o) {
                log.info("=============");
            }

        });

        // in other thread or JVM
        RTopic topic2 = redissonClient.getTopic("anyTopic");
        topic2.publish(new Object());
    }

    @PostMapping("topicPatttern")
    @ApiOperation(value = "Topic patttern")
    public void topicPatttern() throws Exception {

        // subscribe to all topics by `topic1.*` pattern
        RPatternTopic topic1 = redissonClient.getPatternTopic("topic1.*");
        topic1.addListener(Object.class, new PatternMessageListener<Object>() {

            @Override
            public void onMessage(CharSequence charSequence, CharSequence charSequence1, Object o) {

            }
        });
    }
}
