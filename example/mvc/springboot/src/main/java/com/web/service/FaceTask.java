package com.web.service;

import com.web.vo.task.TaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FaceTask implements Runnable {

    private TaskVO taskVO;

    @Override
    public void run() {
        log.info(taskVO.getId() + "======================");
    }

    public void setTaskVO(TaskVO taskVO) {
        this.taskVO = taskVO;
    }
}
