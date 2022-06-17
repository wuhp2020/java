package com.web.waybill.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.web.common.util.IdentityUtil;
import com.web.common.util.NameUtil;
import com.web.waybill.entity.WaybillDO;
import com.web.waybill.mapper.WaybillMapper;
import com.web.waybill.vo.waybill.WaybillUpdateStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;


@Service
@Slf4j
public class WaybillService {

    Random random = new Random();

    @Autowired
    private WaybillMapper waybillMapper;

    @Transactional
    public void save(int id) {
        WaybillDO waybillDO = new WaybillDO();
        String card = IdentityUtil.createIdentity();
        String num = String.format("%020d", id);
        waybillDO.setId(id)
                .setOrderNo(num)
                .setWaybillNo(num)
                .setDeliveryFee(random.nextDouble() * 95 + 5D)
                .setRiderName(NameUtil.getChineseName(IdentityUtil.getGenderByIdCard(card)))
                .setDeliveryType(random.nextInt(10) +"")
                .setDeliveryCompanyCode(num)
                .setDeliveryCompanyName(NameUtil.getDeliveryCompanyName())
                .setStatus(random.nextInt(3) +"")
                .setCreateTime(new Date());
        waybillMapper.insert(waybillDO);
        log.info("执行结束:" + id);
    }

    @Transactional
    public void updateStatus(WaybillUpdateStatusVO statusVO) {
        Wrapper wrapper = Wrappers.<WaybillDO>update().lambda().eq(WaybillDO::getOrderNo, statusVO.getOrderNo())
                .set(WaybillDO::getStatus, statusVO.getStatus());
        waybillMapper.update(null, wrapper);
    }
}
