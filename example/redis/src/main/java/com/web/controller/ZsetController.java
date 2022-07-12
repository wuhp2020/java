package com.web.controller;

import com.web.service.ZsetService;
import com.web.vo.common.ResponseVO;
import com.web.vo.zset.ZsetSaveVO;
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
@RequestMapping("/api/v1/zset")
@Api(tags = "zset类型")
@Slf4j
public class ZsetController {
    @Autowired
    private ZsetService zsetService;

    @PostMapping("save")
    @ApiOperation(value = "增加zset")
    public void save(@RequestBody ZsetSaveVO zsetSaveVO) throws Exception {
        zsetService.save(zsetSaveVO);
    }

    @DeleteMapping("delete/{key}")
    @ApiOperation(value = "删除zset")
    public void delete(@PathVariable("key") String key) throws Exception {
        Pair<Boolean, String> result = zsetService.delete(key);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond() + "");
        }
    }

    @GetMapping("find/{key}")
    @ApiOperation(value = "查询zset")
    public void findOne(@PathVariable("key") String key) throws Exception {
        zsetService.find(key);
    }
}
