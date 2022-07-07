package com.web.repository;

import com.web.model.OrderDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository
        extends CrudRepository<OrderDO, String>, QuerydslPredicateExecutor<OrderDO> {
}
