package com.web.service;

import com.web.vo.api.CreateApiVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/28
 * @ Desc   : 删
 */
@Service
@Slf4j
public class DeleteScriptService implements IScriptService {
    @Override
    public String script(CreateApiVO createApiVO) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    @Override
    public String method() {
        return "DELETE";
    }
}
