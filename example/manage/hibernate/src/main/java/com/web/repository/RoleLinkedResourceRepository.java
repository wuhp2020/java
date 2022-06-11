package com.web.repository;

import com.web.model.RoleLinkedResourceDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleLinkedResourceRepository
        extends CrudRepository<RoleLinkedResourceDO, String>, QuerydslPredicateExecutor<RoleLinkedResourceDO> {

}
