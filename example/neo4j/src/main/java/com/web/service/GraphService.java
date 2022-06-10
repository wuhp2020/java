package com.web.service;

import com.web.model.CityNode;
import com.web.model.CityNodeRelation;
import com.web.repository.CityNodeRelationRepository;
import com.web.repository.CityNodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.internal.InternalPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/11
 * @ Desc   : 描述
 */
@Slf4j
@Service
public class GraphService {

    @Autowired
    private CityNodeRelationRepository cityNodeRelationRepository;

    @Autowired
    private CityNodeRepository cityNodeRepository;

    public void addNode(String city, String direction) {
        CityNode cityNode = new CityNode();
        cityNode.setName(city);
        cityNode.setDirection(direction);
        cityNodeRepository.save(cityNode);
    }

    public void addRelation(Long cityId, Long cityToId, double distance) {
        CityNode cityNode = cityNodeRepository.findNodeById(cityId);
        CityNode cityNodeTo = cityNodeRepository.findNodeById(cityToId);
        if(cityNode != null && cityNodeTo != null) {
            CityNodeRelation cityNodeRelation = new CityNodeRelation();
            cityNodeRelation.setCityNode(cityNode).setCityNodeTo(cityNodeTo).setDistance(distance);
            cityNodeRelationRepository.save(cityNodeRelation);
        }
    }


    public void deleteNodeById(Long id) {
        cityNodeRepository.deleteById(id);
    }

    public void deleteRelationById(Long id) {
        cityNodeRelationRepository.deleteById(id);
    }

    public void updateNode(Long id, String name, String direction) {
        CityNode cityNode = cityNodeRepository.findNodeById(id);
        cityNode.setName(name);
        cityNode.setDirection(direction);
        cityNodeRepository.save(cityNode);
    }

    public List<CityNode> findByName(String name) {
        return cityNodeRepository.findByName(name);
    }

    public CityNode findNodeById(Long id) {
        return cityNodeRepository.findNodeById(id);
    }

    public List<CityNodeRelation> findRelation(String name, String direction) {
        return cityNodeRepository.findRelation(name, direction);
    }

    public List<CityNodeRelation> findShortRelation(String name, String nameTo) {
        List<Map<String, InternalPath.SelfContainedSegment>> result =
                cityNodeRepository.findCityNodeShortRelation(name, nameTo);
        return null;
    }
}
