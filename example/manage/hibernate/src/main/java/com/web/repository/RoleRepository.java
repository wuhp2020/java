package com.web.repository;

import com.web.model.RoleDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository
        extends CrudRepository<RoleDO, String>, QuerydslPredicateExecutor<RoleDO> {

}
