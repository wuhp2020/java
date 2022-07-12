package com.web.controller;

import com.web.vo.builder.UserVO;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/20
 * @ Description: 描述
 */
@RestController
@RequestMapping("/api/v1/design")
@Api(tags = "代理模式")
@Slf4j
public class AgentDesignController {

    @ApiOperation(value = "代理模式")
    @GetMapping("agent")
    public UserVO agent() {
        UserVO userVO = UserVO.builder().age(28).name("wuhp").password("123").nickName("wuheping").build();
        return userVO;
    }
}
