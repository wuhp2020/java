package com.web.service;

import com.web.model.AccountDO;
import com.web.repository.AccountRepository;
import com.web.vo.account.AccountRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountService {

    private static final String ERROR_USER_ID = "1002";

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(rollbackFor = Exception.class)
    public void debit(AccountRequestVO accountRequestVO) {
        AccountDO account = accountRepository.findByUserId(accountRequestVO.getUserId()).get(0);
        account.setMoney(account.getMoney().subtract(accountRequestVO.getOrderMoney()));
        accountRepository.save(account);

        if (ERROR_USER_ID.equals(accountRequestVO.getUserId())) {
            throw new RuntimeException("account branch exception");
        }
    }
}
