package com.spring.task;

import com.querydsl.core.types.Predicate;
import com.web.model.OperationLogDO;
import com.web.model.OperationLogSetupDO;
import com.web.repository.OperationLogRepository;
import com.web.repository.OperationLogSetupRepository;
import com.web.vo.operationlog.OperationLogSetupVO;
import com.web.vo.operationlog.OperationLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
@Configurable
@Slf4j
public class ScheduledTask {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private OperationLogSetupRepository operationLogSetupRepository;

    /**
     * 每5分钟清理一次过时的日志
     */
    @Scheduled(cron = "${task.delete.out-of-date-logs}")
    private void deleteOutOfDateLogs() {
        log.info("class:ScheduledTask, method:deleteOutOfDateLogs ");
        OperationLogSetupVO operationLogSetupVO = new OperationLogSetupVO();
        List<OperationLogSetupDO> operationLogSetupDOs = (List<OperationLogSetupDO>)operationLogSetupRepository.findAll();

        //默认保留天数
        int days = 60;
        if(!CollectionUtils.isEmpty(operationLogSetupDOs)){
            OperationLogSetupDO operationLogSetupDO = operationLogSetupDOs.get(0);
            days = operationLogSetupDO.getDays();
        }

        //分页处理
        int pageNum = 1;
        int pageSize = 10;
        int count = (int)operationLogRepository.count();
        if (count % pageSize != 0){
            pageNum = count/pageSize + 1;
        }else {
            pageNum = count/pageSize;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) - days);
        for (int i = pageNum-1; i >=0; i--){

            Pageable pageable = PageRequest.of(i, pageSize, Sort.Direction.DESC, "id");
            OperationLogVO operationLogVO = new OperationLogVO();
            Predicate predicate = operationLogVO.buildOperationLog();
            Page<OperationLogDO> pageDOs = operationLogRepository.findAll(predicate, pageable);
            for (OperationLogDO operationLogDO: pageDOs){
                Date requestTime = operationLogDO.getRequestTime();
                Calendar calendarTemp = Calendar.getInstance();
                calendarTemp.setTime(requestTime);
                if(calendar.after(calendarTemp)){
                    operationLogRepository.delete(operationLogDO);
                }
            }

        }
    }
}
