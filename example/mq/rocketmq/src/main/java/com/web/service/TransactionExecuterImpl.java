package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionExecuterImpl implements LocalTransactionExecuter {

    @Override
    public LocalTransactionState executeLocalTransactionBranch(Message message, Object o) {
        try {
            //DB操作, 应该带上事务 service -> dao
            //如果数据操作失败, 需要回滚, 同事返回RocketMQ一个失败消息, 意味着, 消费者无法消费到这条失败的消息
            //如果成功, 就要返回一个rocketMQ成功的消息, 意味着消费者将读取到这条消息
            //o就是attachment
            int a = 1 / Integer.valueOf(new String(message.getBody()));
        } catch (Exception e) {
            log.info("===== 本地事务执行失败, 回滚消息队列");
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        log.info("===== 本地事务执行成功, 提交消息队列");
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
