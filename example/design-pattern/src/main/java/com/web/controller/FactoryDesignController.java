package com.web.controller;

import com.web.service.FactoryDesignService;
import com.web.service.factorydesign.Food;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/19
 * @ Description: 描述
 */
@RestController
@RequestMapping("/api/v1/design")
@Api(tags = "工厂模式")
@Slf4j
public class FactoryDesignController {

    @Autowired
    private FactoryDesignService factoryDesignService;

    @ApiOperation(value = "工厂")
    @GetMapping("factory/{name}")
    public ResponseVO simpleFactory(@PathVariable("name") String name) {
        try {
            Food food = factoryDesignService.makeFood(name);
            return ResponseVO.SUCCESS("name: " + name + ", price: " + food.price());
        } catch (Exception e) {
            log.error("失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
