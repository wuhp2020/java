package com.web.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.entity.OrderDO;
import com.web.mapper.OrderMapper;
import com.web.util.IdentityUtil;
import com.web.util.NameUtil;
import com.web.util.PhoneUtil;
import com.web.vo.common.PageQueryVO;
import com.web.vo.order.OrderUpdateStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
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
                .setAddress(IdentityUtil.getProvinceByIdCard(card));
        orderMapper.insert(orderDO);
        log.info("执行结束:" + id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(OrderUpdateStatusVO statusVO) {
        orderService.findByOrderNo(statusVO.getOrderNo());
        orderService.updateStatus(statusVO);
        if ("0".equals(statusVO.getStatus())) {
            orderService.updateStatusIncrement(statusVO);
        }
    }

    public void updateStatus(OrderUpdateStatusVO statusVO) {
        Wrapper wrapper = Wrappers.<OrderDO>update().lambda().eq(OrderDO::getOrderNo, statusVO.getOrderNo())
                .set(OrderDO::getStatus, statusVO.getStatus());
        orderMapper.update(null, wrapper);
        log.info("======== update ========");
    }

    public void updateStatusIncrement(OrderUpdateStatusVO statusVO) {
        Wrapper wrapper = Wrappers.<OrderDO>update().lambda().eq(OrderDO::getOrderNo, (Long.valueOf(statusVO.getOrderNo()) + 1) + "")
                .set(OrderDO::getStatus, statusVO.getStatus());
        orderMapper.update(null, wrapper);
        log.info("======== updateStatusIncrement ========");
        throw new RuntimeException("test exception");
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

    @Transactional(rollbackFor = Exception.class)
    public List<OrderDO> findByDeliveryCompanyName (String deliveryCompanyName) {
        Wrapper wrapperQuery = Wrappers.<OrderDO>query().lambda().and(w -> w.eq(!StringUtils.isEmpty(deliveryCompanyName),
                OrderDO::getDeliveryCompanyName, deliveryCompanyName).or().eq(OrderDO::getStatus, "1")).eq(OrderDO::getOrderNo, "1");
        List<OrderDO> orderDOs = orderMapper.selectList(wrapperQuery);
        return orderDOs;
    }

    public IPage<OrderDO> findByPage(PageQueryVO pageQueryVO) {
        Page<OrderDO> page = new Page(pageQueryVO.getPageNum() , pageQueryVO.getPageSize());
        OrderDO orderDO = new OrderDO();
        Wrapper<OrderDO> wrapper = Wrappers.query(orderDO).lambda()
                .in(OrderDO::getStatus, "1", "2").orderByDesc(OrderDO::getCreateTime);
        IPage<OrderDO> iPage = orderMapper.selectPage(page, wrapper);
        return iPage;
    }
}
