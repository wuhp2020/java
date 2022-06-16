package com.web.service;

import com.web.vo.alert.AlertVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertService {


    public void send(AlertVO alertVO) throws Exception {
        log.info("method: send(), message send ok:"+ alertVO.getMessage());
    }

    public void onMessage(String message){
        // 保证数据幂等性, 实现数据只消费一次
        log.info("method: onMessage(), consumer message: " + message);
    }

    /**
     * 不同分组可重复消费
     * @param message
     */
    public void onMessage2(String message){
        // 保证数据幂等性, 实现数据只消费一次
        log.info("method: onMessage2(), consumer message: " + message);
        // 手动提交
    }
}
