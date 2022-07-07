package com.web.service;

import com.web.entity.InterfaceEntity;
import com.web.vo.InterfaceResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * @ Author : wuheping
 * @ Date   : 2022/6/1
 * @ Desc   : 描述
 */
@Service
@Slf4j
public class PutMyBatisDealService extends AbstractMyBatisDealService {

    @Override
    public Object invoke(HttpServletRequest request) throws Exception {

        InterfaceEntity interfaceEntity = interfaceService.findInterfaceByUrl(request.getRequestURI());
        if (interfaceEntity == null) {
            return "{\"code\": 404, \"msg\": \"暂无此服务, 请配置后调用\"}";
        }
        InterfaceResVO resVO = interfaceService.findInterfaceById(interfaceEntity.getId());

        return "{\"code\": 200, \"msg\": \"ok\"}";
    }

    @Override
    public String xml(InterfaceResVO resVO) {
        return null;
    }

    @Override
    public String method() {
        return "PUT";
    }
}
