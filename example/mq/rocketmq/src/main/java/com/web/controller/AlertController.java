package com.web.controller;

import com.web.service.AlertService;
import com.web.service.TransactionExecuterImpl;
import com.web.vo.alert.AlertVO;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alert")
@Api(tags = "告警管理")
@Slf4j
public class AlertController {

    @Autowired
    private AlertService alertService;

    @Autowired
    private DefaultMQPushConsumer defaultMQPushConsumer;

    @Autowired
    private TransactionMQProducer transactionMQProducer;

    @ApiOperation(value = "发送消息")
    @PostMapping("send")
    public ResponseVO send(@RequestBody AlertVO alertVO) {
        try {
            alertService.send(alertVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("error ", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "分布式事务生产")
    @PostMapping("transactionProducer")
    public ResponseVO transactionProducer(@RequestBody AlertVO alertVO) {
        try {
            TransactionExecuterImpl executer = new TransactionExecuterImpl();
            Message message = new Message("topic-transaction", alertVO.getMessage().getBytes(StandardCharsets.UTF_8));
            transactionMQProducer.sendMessageInTransaction(message, executer, null);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "分布式事务消费")
    @GetMapping("transactionConsumer")
    public ResponseVO transactionConsumer() {
        try {
            defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    for (MessageExt ext: list) {
                        try {
                            log.info("成功消息: " + new String(ext.getBody(),"UTF-8"));
                        } catch (Exception e) {
                            log.info("失败消息: " + new String(ext.getBody()));
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            defaultMQPushConsumer.start();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
