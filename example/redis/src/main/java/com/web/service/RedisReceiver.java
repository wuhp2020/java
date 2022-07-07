package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisReceiver {

    public void receiverMessage (String alert) {
        log.info("alert workorders: " + alert);
    }
}
