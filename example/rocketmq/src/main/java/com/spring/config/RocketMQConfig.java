package com.spring.config;

import com.web.service.TransactionCheckListenerImpl;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;

    @Value("${rocketmq.name-server}")
    private String nameserAddr;

    @Bean
    public DefaultMQPushConsumer consumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameserAddr);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //消费事务消息
        consumer.subscribe("topic-transaction","*");

        return consumer;
    }

    @Bean
    public TransactionMQProducer producer() throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer(producerGroup);
        //MQ服务器地址
        producer.setNamesrvAddr(nameserAddr);
        //注册事务回查监听
        producer.setTransactionCheckListener(new TransactionCheckListenerImpl());
        return producer;
    }
}
