package com.web.controller;

import com.web.service.ComplaintService;
import com.web.vo.complaint.ClaimOrderReqVO;
import com.web.vo.complaint.ClaimOrderResVO;
import com.web.vo.complaint.ComplaintOrderReqVO;
import com.web.vo.complaint.ComplaintOrderResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/openele")
@Api(tags = "投诉管理")
@Slf4j
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("complaintOrder")
    @ApiOperation(value = "投诉订单")
    public ComplaintOrderResVO complaintOrder(@RequestBody ComplaintOrderReqVO complaintOrderReqVO) {
        return null;
    }

    @PostMapping("claimOrder")
    @ApiOperation(value = "索赔订单")
    public ClaimOrderResVO claimOrder(@RequestBody ClaimOrderReqVO claimOrderReqVO) {
        return null;
    }

}
