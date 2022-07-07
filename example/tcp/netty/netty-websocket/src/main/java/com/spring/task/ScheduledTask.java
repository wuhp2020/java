package com.spring.task;

import com.spring.config.NettyConfig;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableScheduling
@Slf4j
public class ScheduledTask {

    @Scheduled(cron = "0/3 * * * * ?")
    private void alert() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        String date = sdf.format(new Date());
        String message = "告警！ "+ date;
        TextWebSocketFrame tws = new TextWebSocketFrame(message);
        NettyConfig.GROUP.writeAndFlush(tws);
        log.info(message);
    }
}
