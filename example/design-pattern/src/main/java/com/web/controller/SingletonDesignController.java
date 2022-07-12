package com.web.controller;

import com.web.vo.singleton.AfterSingletonVO;
import com.web.vo.singleton.BeforeSingletonVO;
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
@Api(tags = "单例模式")
@Slf4j
public class SingletonDesignController {

    @ApiOperation(value = "before单例模式")
    @GetMapping("beforeSingleton")
    public BeforeSingletonVO beforeSingleton() {
        return BeforeSingletonVO.getInstance();
    }

    @ApiOperation(value = "after单例模式")
    @GetMapping("afterSingleton")
    public AfterSingletonVO afterSingleton() {
        return AfterSingletonVO.getInstance();
    }
}
