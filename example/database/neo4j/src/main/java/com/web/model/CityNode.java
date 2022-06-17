package com.web.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;


/**
 * @ Author : wuheping
 * @ Date   : 2022/5/11
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
@NodeEntity
public class CityNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String direction;
}
