package com.web.repository;

import com.web.model.ResourceDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository
        extends CrudRepository<ResourceDO, String>, QuerydslPredicateExecutor<ResourceDO> {

}
