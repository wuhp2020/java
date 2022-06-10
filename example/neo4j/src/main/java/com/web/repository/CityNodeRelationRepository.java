package com.web.repository;

import com.web.model.CityNodeRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/11
 * @ Desc   : 描述
 */
@Repository
public interface CityNodeRelationRepository extends Neo4jRepository<CityNodeRelation, Long> {


}
