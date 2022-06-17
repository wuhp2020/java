package com.web.service;

import com.web.service.api.OrderApiService;
import com.web.service.api.StorageApiService;
import com.web.vo.business.BusinessRequestVO;
import com.web.vo.common.ResponseVO;
import com.web.vo.order.OrderRequestVO;
import com.web.vo.storage.StorageRequestVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BusinessService {

    @Autowired
    private StorageApiService storageApiService;

    @Autowired
    private OrderApiService orderApiService;

    /**
     * 减库存, 下订单
     * @param businessRequestVO
     */
    @GlobalTransactional
    public void purchase(BusinessRequestVO businessRequestVO) {
        OrderRequestVO orderRequestVO = new OrderRequestVO();
        StorageRequestVO storageRequestVO = new StorageRequestVO();
        BeanUtils.copyProperties(businessRequestVO, storageRequestVO);
        BeanUtils.copyProperties(businessRequestVO, orderRequestVO);
        ResponseVO storageResponseVO = storageApiService.deduct(storageRequestVO);
        if (!"200".equals(storageResponseVO.getCode())) {
            throw new RuntimeException("仓库服务异常");
        }

        ResponseVO orderResponseVO = orderApiService.create(orderRequestVO);
        if (!"200".equals(orderResponseVO.getCode())) {
            throw new RuntimeException("订单服务异常");
        }

    }
}
