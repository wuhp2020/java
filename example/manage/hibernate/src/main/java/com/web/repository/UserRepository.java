package com.web.repository;

import com.web.model.UserDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
        extends CrudRepository<UserDO, String>, QuerydslPredicateExecutor<UserDO> {

}
