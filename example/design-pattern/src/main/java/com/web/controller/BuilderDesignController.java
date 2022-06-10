package com.web.controller;

import com.web.vo.builder.UserVO;
import com.web.vo.common.ResponseVO;
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
    public ResponseVO builderUser() {
        try {
            return ResponseVO.SUCCESS(UserVO.builder().age(28).name("wuhp").password("123").nickName("wuheping").build());
        } catch (Exception e) {
            log.error("失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
