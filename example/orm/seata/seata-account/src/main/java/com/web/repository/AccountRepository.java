package com.web.repository;

import com.web.model.AccountDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository
        extends CrudRepository<AccountDO, String>, QuerydslPredicateExecutor<AccountDO> {
    public List<AccountDO> findByUserId(String userId);
}
