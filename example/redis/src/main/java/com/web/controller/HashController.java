package com.web.controller;

import com.web.service.HashService;
import com.web.vo.common.ResponseVO;
import com.web.vo.hash.HashSaveVO;
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

@RestController
@RequestMapping("/api/v1/hash")
@Api(tags = "hash类型")
@Slf4j
public class HashController {
    @Autowired
    private HashService hashService;

    @PostMapping("save")
    @ApiOperation(value = "增加hash")
    public void save(@RequestBody HashSaveVO hashSaveVO) throws Exception {
        hashService.save(hashSaveVO);
    }

    @DeleteMapping("delete/{id}/{key}")
    @ApiOperation(value = "删除hash")
    public void delete(@PathVariable("id") String id, @PathVariable("key") String key) {
        Pair<Boolean, String> result = hashService.delete(id, key);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond());
        }
    }

    @GetMapping("findOne/{id}/{key}")
    @ApiOperation(value = "查询单个hash")
    public Object findOne(@PathVariable("id") String id, @PathVariable("key") String key) throws Exception {
        return hashService.findOne(id, key);
    }
}
