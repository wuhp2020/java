package com.web.service;

import com.web.vo.alert.AlertVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.jms.Queue;

@Service
@Slf4j
public class AlertService {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    public void send(AlertVO alertVO) throws Exception {
        jmsMessagingTemplate.convertAndSend(queue, alertVO.getMessage());
        log.info("method: send(), message send ok:"+ alertVO.getMessage());
    }

    @JmsListener(destination = "alertTopic")
    @SendTo("SQueue")
    public void onMessage(String message){
        log.info("method: onMessage(), consumer message: " + message);
    }
}
