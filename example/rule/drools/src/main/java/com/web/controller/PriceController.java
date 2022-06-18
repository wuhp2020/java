package com.web.controller;

import com.web.vo.common.ResponseVO;
import com.web.vo.price.PriceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/price")
@Api(tags = "价格管理")
@Slf4j
public class PriceController {

    @Resource(name = "kieSessionMap")
    private Map<String, KieSession> kieSessionMap;

    @ApiOperation(value = "汽车价格查询")
    @PostMapping("car/find")
    public ResponseVO findCar(@RequestBody PriceVO priceVO) {
        try {
            KieSession kieSession = kieSessionMap.get(priceVO.getFileName());
            kieSession.insert(priceVO);
            kieSession.fireAllRules();
            return ResponseVO.SUCCESS(priceVO);
        } catch (Exception e) {
            log.error("查询汽车价格失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
