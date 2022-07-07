package com.spring.task;

import com.web.service.AlertWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableScheduling
@Slf4j
public class ScheduledTask {

    @Autowired
    private AlertWebSocket alertWebSocket;

    @Scheduled(cron = "0/3 * * * * ?")
    private void alert() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String date = sdf.format(new Date());
        String message = "告警！ "+ date;
        alertWebSocket.sendMessage(message);
        log.info(message);
    }
}
