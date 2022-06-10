package com.web.service;

import com.web.vo.alert.AlertVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(AlertVO alertVO) throws Exception {
        kafkaTemplate.send("alertTopic", 1, "alertTopic", alertVO.getMessage());
        log.info("method: send(), message send ok:"+ alertVO.getMessage());
    }

    @KafkaListener(topics = {"alertTopic"})
    public void onMessage1(String message, Acknowledgment ack){
        // 保证数据幂等性, 实现数据只消费一次
        log.info("method: onMessage1(), consumer message: " + message);
        ack.acknowledge();
    }

    /**
     * 不同分组可重复消费
     * @param message
     * @param ack
     */
    @KafkaListener(topics = {"alertTopic"}, groupId = "aaa")
    public void onMessage2(String message, Acknowledgment ack){
        // 保证数据幂等性, 实现数据只消费一次
        log.info("method: onMessage2(), consumer message: " + message);
        // 手动提交
        ack.acknowledge();
    }

    @KafkaListener(topics = {"alertTopic"}, groupId = "bbb")
    public void onMessage3(ConsumerRecord<String, String> records){
        String data = records.value();
        Integer partition = records.partition();
        Long timestamp = records.timestamp();
        Long offset = records.offset();
        // 保证数据幂等性, 实现数据只消费一次
        log.info("kafka consumer -> partition:{}; offset:{}; timestamp:{}", partition, offset, timestamp);
    }
}
