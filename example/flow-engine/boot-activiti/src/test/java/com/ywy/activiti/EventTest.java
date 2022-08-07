package com.ywy.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 事件测试
 *
 * @author ywy
 * @date 2021/11/2 13:22
 */
@SpringBootTest
public class EventTest {
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 信号事件
     */
    @Test
    public void signalStart() {
        runtimeService.signalEventReceived("Signal_1auq05c");
    }

    /**
     * 消息事件
     */
    @Test
    public void msgBack() {
        Execution execution = runtimeService.createExecutionQuery().messageEventSubscriptionName("Message_3dgvqio")
                .processInstanceId("").singleResult();
        runtimeService.messageEventReceived("Message_3dgvqio", execution.getId());
    }
}
