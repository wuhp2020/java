package com.web.service.api;

import com.web.vo.account.AccountRequestVO;
import com.web.vo.common.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "account-service")
public interface AccountApiService {
    @PostMapping("/api/v1/account/debit")
    public ResponseVO debit(@RequestBody AccountRequestVO accountVO);
}
