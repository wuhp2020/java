package com.web.repository;

import com.web.model.OperationLogSetupDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogSetupRepository
        extends CrudRepository<OperationLogSetupDO, String>, QuerydslPredicateExecutor<OperationLogSetupDO> {

}
