package com.web.repository;

import com.web.model.OperationLogDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository
        extends CrudRepository<OperationLogDO, String>, QuerydslPredicateExecutor<OperationLogDO> {

}
