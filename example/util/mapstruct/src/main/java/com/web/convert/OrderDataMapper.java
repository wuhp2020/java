package com.web.convert;

import com.web.entity.OrderDO;
import com.web.vo.order.OrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/6
 * @ Desc   : 描述
 */
@Mapper
public interface OrderDataMapper {
    OrderDataMapper INSTANCES = Mappers.getMapper(OrderDataMapper.class);

    OrderVO orderDO2OrderVO(OrderDO orderDO);
}
