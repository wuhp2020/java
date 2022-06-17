package com.web.service;

import com.web.model.OrderDO;
import com.web.repository.OrderRepository;
import com.web.service.api.AccountApiService;
import com.web.vo.account.AccountRequestVO;
import com.web.vo.common.ResponseVO;
import com.web.vo.order.OrderRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountApiService accountApiService;

    @Transactional
    public void create(OrderRequestVO orderRequestVO) {
        BigDecimal orderMoney = new BigDecimal(orderRequestVO.getOrderCount()).multiply(new BigDecimal(5));
        OrderDO order = new OrderDO();
        order.setUserId(orderRequestVO.getUserId());
        order.setCommodityCode(orderRequestVO.getCommodityCode());
        order.setCount(orderRequestVO.getOrderCount());
        order.setMoney(orderMoney);

        orderRepository.save(order);
        AccountRequestVO accountRequestVO = new AccountRequestVO();
        accountRequestVO.setOrderMoney(orderMoney);
        accountRequestVO.setUserId(orderRequestVO.getUserId());
        ResponseVO accountResponseVO = accountApiService.debit(accountRequestVO);
        if (!"200".equals(accountResponseVO.getCode())) {
            throw new RuntimeException("账户服务异常");
        }

    }
}
