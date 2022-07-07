package com.web.service;

import com.google.gson.Gson;
import com.web.util.HttpClientUtil;
import com.web.vo.order.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataService {

    @Value("${order.url}")
    private String orderURL;

    @Value("${dict.url}")
    private String dictURL;

    /**
     * 查询订单
     * @throws Exception
     */
    public OrderVO findOrder() throws Exception {
        String response = HttpClientUtil.httpPost(orderURL, "{}");
        return new Gson().fromJson(response, OrderVO.class);
    }

    public void saveDict() throws Exception {
        String response = HttpClientUtil.httpPost(dictURL, "{}");
        log.info("saveDict(): "+ response);
    }
}
