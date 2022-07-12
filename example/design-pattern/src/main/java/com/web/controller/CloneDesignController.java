package com.web.controller;

import com.web.vo.clone.FruitVO;
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
@Api(tags = "克隆模式")
@Slf4j
public class CloneDesignController {

    @ApiOperation(value = "克隆模式")
    @GetMapping("clone")
    public FruitVO cloneT() throws Exception {
        FruitVO fruitVO = new FruitVO().setName("apple");
        FruitVO fruitVOClone = (FruitVO)fruitVO.clone();
        return fruitVOClone;
    }
}
