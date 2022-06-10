package com.web.repository;

import com.web.model.UserLinkedRoleDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLinkedRoleRepository
        extends CrudRepository<UserLinkedRoleDO, String>, QuerydslPredicateExecutor<UserLinkedRoleDO> {
}
