package com.web.service;

import com.web.vo.alert.AlertVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RocketMQMessageListener(topic = "topic", consumerGroup = "producer")
public class AlertService implements RocketMQListener<AlertVO> {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void send(AlertVO alertVO) throws Exception {
        rocketMQTemplate.convertAndSend("topic", alertVO);
        log.info("method: send(), message send ok:"+ alertVO.getMessage());
    }

    @Override
    public void onMessage(AlertVO alertVO) {
        log.info("================== " + alertVO.getMessage());
    }

}



