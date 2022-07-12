package com.web.controller;

import com.web.vo.builder.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/20
 * @ Description: 描述
 */
@RestController
@RequestMapping("/api/v1/design")
@Api(tags = "建造者模式")
@Slf4j
public class BuilderDesignController {

    @ApiOperation(value = "建造者模式")
    @GetMapping("builderUser")
    public UserVO builderUser() {
        UserVO userVO = UserVO.builder().age(28).name("wuhp").password("123").nickName("wuheping").build();
        return userVO;
    }

}
