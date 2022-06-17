package com.web.controller;

import com.web.service.BusinessService;
import com.web.vo.business.BusinessRequestVO;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/business")
@Api(tags = "主业务")
@Slf4j
public class BusinessController {

    @Autowired
    public BusinessService businessService;

    /**
     * 购买下单, 模拟全局事务提交
     *
     * @return
     */
    @PostMapping("commit")
    @ApiOperation(value = "提交")
    public ResponseVO commit() {
        try {
            BusinessRequestVO businessRequestVO = new BusinessRequestVO();
            businessRequestVO.setCommodityCode("2001");
            businessRequestVO.setUserId("1001");
            businessRequestVO.setOrderCount(1);
            businessService.purchase(businessRequestVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:commit() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    /**
     * 购买下单, 模拟全局事务回滚
     *
     * @return
     */
    @PostMapping("rollback")
    @ApiOperation(value = "回滚")
    public ResponseVO rollback() {

        try {
            BusinessRequestVO businessRequestVO = new BusinessRequestVO();
            businessRequestVO.setCommodityCode("2001");
            businessRequestVO.setUserId("1002");
            businessRequestVO.setOrderCount(1);
            businessService.purchase(businessRequestVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:rollback() 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
