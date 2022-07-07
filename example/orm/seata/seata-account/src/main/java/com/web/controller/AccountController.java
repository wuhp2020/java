package com.web.controller;

import com.web.service.AccountService;
import com.web.vo.account.AccountRequestVO;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@Api(tags = "账户服务")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "debit")
    @ApiOperation(value = "从用户账户中借出")
    public ResponseVO debit(@RequestBody AccountRequestVO accountRequestVO) {
        try {
            accountService.debit(accountRequestVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:debit 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
