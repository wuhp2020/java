package com.web.repository;

import com.web.model.CityNode;
import com.web.model.CityNodeRelation;
import org.neo4j.driver.internal.InternalPath;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/11
 * @ Desc   : 描述
 */
@Repository
public interface CityNodeRepository extends Neo4jRepository<CityNode, Long> {

    @Query("MATCH (c:CityNode) WHERE c.name=$name RETURN c")
    List<CityNode> findByName(String name);

    /**
     * 根据节点名称查找关系
     * @param name
     * @return
     */
    @Query("MATCH q=(c:CityNode)-[r:CITYNODERELATION]-(ct:CityNode) WHERE c.name=$name and c.direction=$direction RETURN q")
    List<CityNodeRelation> findRelation(String name, String direction);

    @Query("MATCH relation=(c:CityNode)-[*]->(ct:CityNode) WHERE c.name=$name and ct.name=$nameTo RETURN relation")
    List<Map<String, InternalPath.SelfContainedSegment>> findCityNodeShortRelation(String name, String nameTo);

    @Query("MATCH (c:CityNode) WHERE id(c)=$id RETURN c")
    CityNode findNodeById(Long id);
}
