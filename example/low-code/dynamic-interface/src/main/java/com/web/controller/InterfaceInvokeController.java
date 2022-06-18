package com.web.controller;

import com.web.service.AbstractMyBatisDealService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/2
 * @ Desc   : 描述
 */
@Slf4j
@Api(tags = "接口执行")
@Controller
public class InterfaceInvokeController {

    @Autowired
    private List<AbstractMyBatisDealService> myBatisDealServices;

    @RequestMapping(value = "/v1/**")
    @ResponseBody
    public Object getInvoke(HttpServletRequest request) throws Exception {
        AbstractMyBatisDealService myBatisDealService = myBatisDealServices.stream()
                .filter(iMyBatisFileService -> iMyBatisFileService.method()
                        .equals(request.getMethod()))
                .findFirst().get();
        return myBatisDealService.invoke(request);
    }
}
