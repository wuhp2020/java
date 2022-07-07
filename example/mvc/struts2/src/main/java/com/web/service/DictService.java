package com.web.service;

import com.web.vo.dict.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DictService {

    /**
     * 增
     * @param dictVO
     * @return
     * @throws Exception
     */
    public void save(DictVO dictVO) throws Exception{
        try {
            log.info("save() ok...");
        } catch (Exception e) {
            log.error("method:save 异常", e);
            throw new Exception(e.getMessage());
        }
    }
}
