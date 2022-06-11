package com.web.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.web.model.OperationLogDO;
import com.web.model.OperationLogSetupDO;
import com.web.repository.OperationLogRepository;
import com.web.repository.OperationLogSetupRepository;
import com.web.vo.operationlog.OperationLogQueryVO;
import com.web.vo.operationlog.OperationLogSetupVO;
import com.web.vo.operationlog.OperationLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class OperationLogService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private OperationLogSetupRepository operationLogSetupRepository;

    /**
     * 单个查询
     * @param id
     * @return
     * @throws Exception
     */
    public OperationLogVO findOne(String id) throws Exception{
        OperationLogDO operationLogDO = operationLogRepository.findById(id).get();
        OperationLogVO operationLogVO = new OperationLogVO();
        BeanUtils.copyProperties(operationLogDO, operationLogVO);
        return operationLogVO;
    }

    /**
     * 查询设置日志保存时间
     * @return
     * @throws Exception
     */
    public OperationLogSetupVO findLogSetup() throws Exception{
        List<OperationLogSetupDO> operationLogSetupDOs = (List<OperationLogSetupDO>)operationLogSetupRepository.findAll();
        OperationLogSetupVO operationLogSetupVO = new OperationLogSetupVO();
        if (!CollectionUtils.isEmpty(operationLogSetupDOs)){
            OperationLogSetupDO operationLogSetupDO = operationLogSetupDOs.get(0);
            BeanUtils.copyProperties(operationLogSetupDO, operationLogSetupVO);
        }
        return operationLogSetupVO;
    }

    /**
     * 设置日志保存时间
     * @param operationLogSetupVO
     * @return
     * @throws Exception
     */
    public OperationLogSetupVO saveLogSetup(OperationLogSetupVO operationLogSetupVO) throws Exception{
        operationLogSetupRepository.deleteAll();
        OperationLogSetupDO operationLogSetupDO = new OperationLogSetupDO();
        BeanUtils.copyProperties(operationLogSetupVO, operationLogSetupDO);
        operationLogSetupDO = operationLogSetupRepository.save(operationLogSetupDO);
        operationLogSetupVO.setId(operationLogSetupDO.getId());
        return operationLogSetupVO;
    }

    /**
     * 分页查询
     * @param operationLogQueryVO
     * @return
     * @throws Exception
     */
    public Page<OperationLogVO> findByPage(OperationLogQueryVO operationLogQueryVO) throws Exception{
        Pageable pageable = PageRequest.of(operationLogQueryVO.getPageNum(),
                operationLogQueryVO.getPageSize(), Sort.Direction.DESC, "id");
        Predicate predicate = operationLogQueryVO.buildOperationLog();

        Page<OperationLogDO> pageDOs = operationLogRepository.findAll(predicate, pageable);
        List<OperationLogVO> operationLogVOs = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageDOs.getContent())) {
            List<OperationLogDO> operationLogDOs = pageDOs.getContent();
            operationLogDOs.stream().forEach(operationLogDOResult -> {
                OperationLogVO operationLogVO = new OperationLogVO();
                BeanUtils.copyProperties(operationLogDOResult, operationLogVO);
                operationLogVOs.add(operationLogVO);
            });
        }
        return new PageImpl<OperationLogVO>(operationLogVOs, pageable, pageDOs.getTotalElements());
    }
}
