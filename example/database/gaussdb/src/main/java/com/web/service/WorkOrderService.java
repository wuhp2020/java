package com.web.service;

import com.web.entity.WorkOrderDO;
import com.web.mapper.WorkOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class WorkOrderService {

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Transactional
    public void save(WorkOrderDO workOrderDO) {
        workOrderMapper.insert(workOrderDO);
    }

    @Transactional
    public Map<String, Integer> findAll() {
        List<WorkOrderDO> list = workOrderMapper.findAll();
        return list.parallelStream().collect(Collectors.toMap(WorkOrderDO::getWorkOrderId, WorkOrderDO::getRelatedSize));
    }
}
