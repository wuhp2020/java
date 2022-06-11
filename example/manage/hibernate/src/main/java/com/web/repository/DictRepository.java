package com.web.repository;

import com.web.model.DictDO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictRepository
        extends CrudRepository<DictDO, String>, QuerydslPredicateExecutor<DictDO> {

    public List<DictDO> findByCodeAndType(String type, String code) throws Exception;

    @Query(value = "select type,code from DictDO")
    public List<Object[]> findAllCodeAndType() throws Exception;
}
