package com.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.entity.WorkOrderDO;

import java.util.List;

public interface WorkOrderMapper extends BaseMapper<WorkOrderDO> {
    List<WorkOrderDO> findAll();
}