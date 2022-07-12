package com.web.controller;

import com.web.model.CityNode;
import com.web.model.CityNodeRelation;
import com.web.service.GraphService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/11
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/graph")
@Api(tags = "节点管理")
@Slf4j
public class GraphController {

    @Autowired
    private GraphService graphService;

    @ApiOperation(value = "添加节点")
    @PostMapping(path = "/create")
    public void addNode(
            @RequestParam(name = "city", defaultValue = "北京") String city,
            @RequestParam(name = "direction", defaultValue = "N") String direction) {
        graphService.addNode(city, direction);
    }

    @ApiOperation(value = "添加关系")
    @PostMapping(path = "/addRelation")
    public void addRelation(
            @RequestParam(name = "cityId", defaultValue = "1") Long cityId,
            @RequestParam(name = "cityToId", defaultValue = "2") Long cityToId,
            @RequestParam(name = "distance", defaultValue = "100200.58") double distance) {
        graphService.addRelation(cityId, cityToId, distance);
    }

    @ApiOperation(value = "删除节点")
    @DeleteMapping(path = "/delete")
    public void deleteNode(
            @RequestParam(name = "id", required = false)Long id) {
        graphService.deleteNodeById(id);
    }

    @ApiOperation(value = "删除关系")
    @DeleteMapping(path = "/deleteRelation")
    public void deleteRelation(
            @RequestParam(name = "id", required = false)Long id) {
        graphService.deleteRelationById(id);
    }

    @ApiOperation(value = "更新节点")
    @PutMapping(path = "/update")
    public void updateNode(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "direction", defaultValue = "N") String direction) {
        graphService.updateNode(id, name, direction);
    }

    @ApiOperation(value = "根据名字、方向查找相关的所有相邻节点")
    @GetMapping(path = "/findRelation")
    public List<CityNodeRelation> findRelation(
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "direction", defaultValue = "N") String direction) {
        List<CityNodeRelation> cityNodeRelations = graphService.findRelation(name, direction);
        return cityNodeRelations;
    }

    @ApiOperation(value = "查询两个节点的最短路径")
    @GetMapping(path = "/findShortRelation")
    public List<CityNodeRelation> findShortRelation(
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "nameTo", required = true) String nameTo) {
        List<CityNodeRelation> cityNodeRelations = graphService.findShortRelation(name, nameTo);
        return cityNodeRelations;
    }

    @ApiOperation(value = "根据name查找节点")
    @GetMapping(path = "/findByName")
    public List<CityNode> findByName(
            @RequestParam(name = "name", required = true) String name) {
        List<CityNode> cityNodes = graphService.findByName(name);
        return cityNodes;
    }

    @ApiOperation(value = "根据id查找节点")
    @GetMapping(path = "/findById")
    public CityNode findById(
            @RequestParam(name = "id", required = true) Long id) {
        CityNode cityNode = graphService.findNodeById(id);
        return cityNode;
    }
}
