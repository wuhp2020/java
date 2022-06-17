package com.web.service.api;

import com.web.vo.common.ResponseVO;
import com.web.vo.order.OrderRequestVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service")
public interface OrderApiService {
    @PostMapping("/api/v1/order/create")
    public ResponseVO create(@RequestBody OrderRequestVO orderRequestVO);
}
