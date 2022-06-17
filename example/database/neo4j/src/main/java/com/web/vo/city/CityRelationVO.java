package com.web.vo.city;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/11
 * @ Desc   : 描述
 */
@Data
@Accessors(chain = true)
public class CityRelationVO {

    private Double distance;

    @StartNode
    private CityVO cityNode;

    @EndNode
    private CityVO cityNodeTo;
}
