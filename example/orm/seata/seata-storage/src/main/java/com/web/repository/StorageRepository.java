package com.web.repository;

import com.web.model.StorageDO;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StorageRepository
        extends CrudRepository<StorageDO, String>, QuerydslPredicateExecutor<StorageDO> {
    public List<StorageDO> findByCommodityCode(String commodityCode);
}
