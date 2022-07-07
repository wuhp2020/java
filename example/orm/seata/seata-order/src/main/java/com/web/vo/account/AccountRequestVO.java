package com.web.vo.account;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequestVO {

    String userId;
    BigDecimal orderMoney;
}
