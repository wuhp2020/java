package com.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/11
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
@RelationshipEntity
public class CityNodeRelation {
    @Id
    @GeneratedValue
    private Long id;
    private Double distance;

    @StartNode
    private CityNode cityNode;

    @EndNode
    private CityNode cityNodeTo;
}
