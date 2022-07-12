package com.web.controller;

import com.web.service.DataService;
import com.web.vo.common.ResponseVO;
import com.web.vo.order.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/data")
@Api(tags = "数据管理")
@Slf4j
public class DataController {

    @Autowired
    private DataService dataService;

    @ApiOperation(value = "查询订单")
    @PostMapping("order/find")
    public OrderVO findOrder() throws Exception {
        return dataService.findOrder();
    }

    @ApiOperation(value = "保存字典")
    @PostMapping("dict/save")
    public void saveDict() throws Exception {
        dataService.saveDict();
    }
}
