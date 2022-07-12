package com.web.controller;

import com.web.service.SetService;
import com.web.vo.common.ResponseVO;
import com.web.vo.set.SetComparatorVO;
import com.web.vo.set.SetSaveVO;
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
@RequestMapping("/api/v1/set")
@Api(tags = "set类型")
@Slf4j
public class SetController {
    @Autowired
    private SetService setService;

    @PostMapping("save")
    @ApiOperation(value = "增加set")
    public void save(@RequestBody SetSaveVO setSaveVO) throws Exception {
        setService.save(setSaveVO);
    }

    @DeleteMapping("delete/{key}")
    @ApiOperation(value = "删除set")
    public void delete(@PathVariable("key") String key) {
        Pair<Boolean, Object> result = setService.delete(key);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond() + "");
        }
    }

    @GetMapping("findOne/{key}")
    @ApiOperation(value = "查询单个set")
    public void findOne(@PathVariable("key") String key) throws Exception {
        setService.findOne(key);
    }

    @PostMapping("union")
    @ApiOperation(value = "并集set")
    public Object union(@RequestBody SetComparatorVO setUnionVO) throws Exception {
        return setService.union(setUnionVO.getKey1(), setUnionVO.getKey2());
    }

    @PostMapping("inter")
    @ApiOperation(value = "交集set")
    public Object inter(@RequestBody SetComparatorVO setInterVO) throws Exception {
        return setService.inter(setInterVO.getKey1(), setInterVO.getKey2());
    }

    @PostMapping("diff")
    @ApiOperation(value = "差集set")
    public Object diff(@RequestBody SetComparatorVO setDiffVO) throws Exception {
        return setService.diff(setDiffVO.getKey1(), setDiffVO.getKey2());
    }
}
