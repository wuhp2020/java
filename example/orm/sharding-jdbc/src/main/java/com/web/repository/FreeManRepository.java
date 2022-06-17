package com.web.repository;

import com.web.model.FreeManDO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeManRepository extends CrudRepository<FreeManDO, Long>{

    FreeManDO findByIdentity(String identity);

}
