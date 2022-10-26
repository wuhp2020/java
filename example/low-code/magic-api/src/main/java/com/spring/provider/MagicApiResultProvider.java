package com.spring.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.context.RequestEntity;
import org.ssssssss.magicapi.core.interceptor.ResultProvider;
import org.ssssssss.magicapi.modules.db.model.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/26
 * @ Desc   : 描述
 */
@Slf4j
@Component
public class MagicApiResultProvider implements ResultProvider {

    /**
     *   定义返回结果，默认返回JsonBean
     */
    @Override
    public Object buildResult(RequestEntity requestEntity, int code, String message, Object data) {
        // 如果对分页格式有要求的话，可以对data的类型进行判断，进而返回不同的格式
        return new HashMap(){
            {
                put("code", code);
                put("message", message);
                put("body", data);
            }
        };
    }

    /**
     *   定义分页返回结果，该项会被封装在Json结果内，
     *   此方法可以不覆盖，默认返回PageResult
     */
    @Override
    public Object buildPageResult(RequestEntity requestEntity, Page page, long total, List<Map<String, Object>> data) {
        return new HashMap<String,Object>(){
            {
                put("total", total);
                put("records", data);
            }
        };
    }

    @Override
    public Object buildException(RequestEntity requestEntity, Throwable throwable) {
        return buildResult(requestEntity, 500, throwable.getMessage());
    }

}
