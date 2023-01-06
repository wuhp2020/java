package com.web.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class EmployeeService {

    public void changeParams(Map<String, Object> params) {
        log.info("=============== " + JSON.toJSONString(params));
        params.put("name", "吴和平");
    }
}
