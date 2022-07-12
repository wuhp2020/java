package com.web.controller;

import com.web.service.ListService;
import com.web.vo.common.ResponseVO;
import com.web.vo.list.ListSaveVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/list")
@Api(tags = "list类型")
@Slf4j
public class ListController {
    @Autowired
    private ListService listService;

    @PostMapping("save")
    @ApiOperation(value = "增加list")
    public void save(@RequestBody ListSaveVO listSaveVO) throws Exception {
        listService.save(listSaveVO.getKey(), listSaveVO.getValue());
    }

    @DeleteMapping("delete/{key}")
    @ApiOperation(value = "删除list")
    public void delete(@PathVariable("key") String key) {
        Pair<Boolean, Object> result = listService.delete(key);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond() + "");
        }
    }

    @GetMapping("findOne/{key}")
    @ApiOperation(value = "查询单个list")
    public List<String> findOne(@PathVariable("key") String key) throws Exception {
            return listService.findAll(key);
    }
}
