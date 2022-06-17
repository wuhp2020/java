package com.web.order.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.web.common.util.IdentityUtil;
import com.web.common.util.NameUtil;
import com.web.common.util.PhoneUtil;
import com.web.order.entity.OrderDO;
import com.web.order.mapper.OrderMapper;
import com.web.vo.order.OrderUpdateStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;


@Service
@Slf4j
public class OrderService {

    Random random = new Random();


    @Autowired
    public OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void save(int id) {
        OrderDO orderDO = new OrderDO();
        String card = IdentityUtil.createIdentity();
        String businessName = NameUtil.getBusinessName();
        String num = String.format("%020d", id);
        orderDO.setId(id)
                .setOrderNo(num)
                .setTakeGoodsCode(num)
                .setChannelSourceCode(num)
                .setChannelSourceName(NameUtil.getChannelSourceName())
                .setBusinessCode(num)
                .setBusinessName(businessName)
                .setBusinessPhone(PhoneUtil.getPhone())
                .setMoney(random.nextDouble() * 95 + 5D)
                .setUserName(NameUtil.getChineseName(IdentityUtil.getGenderByIdCard(card)))
                .setStoreCode(num)
                .setStoreName(businessName)
                .setIsPay(random.nextInt(2) +"")
                .setDeliveryType(random.nextInt(10) +"")
                .setDeliveryCompanyCode(num)
                .setDeliveryCompanyName(NameUtil.getDeliveryCompanyName())
                .setStatus(random.nextInt(3) +"")
                .setAddress(IdentityUtil.getProvinceByIdCard(card))
                .setCreateTime(new Date());
        orderMapper.insert(orderDO);
        log.info("执行结束:" + id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(OrderUpdateStatusVO statusVO) {
        Wrapper wrapper = Wrappers.<OrderDO>update().lambda().eq(OrderDO::getOrderNo, statusVO.getOrderNo())
                .set(OrderDO::getStatus, statusVO.getStatus());
        orderMapper.update(null, wrapper);
        log.info("======== update ========");
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDO findByOrderNo (String orderNo) {
        Wrapper wrapperQuery = Wrappers.<OrderDO>query().lambda().eq(OrderDO::getOrderNo, orderNo).last(" for update ");
        OrderDO orderDO = orderMapper.selectOne(wrapperQuery);
        return orderDO;
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderDO findByTakeGoodsCode (String takeGoodsCode) {
        Wrapper wrapperQuery = Wrappers.<OrderDO>query().lambda().eq(OrderDO::getTakeGoodsCode, takeGoodsCode);
        OrderDO orderDO = orderMapper.selectOne(wrapperQuery);
        return orderDO;
    }
}
