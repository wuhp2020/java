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
    public ResponseVO save(@RequestBody SetSaveVO setSaveVO) {
        try {
            setService.save(setSaveVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @DeleteMapping("delete/{key}")
    @ApiOperation(value = "删除set")
    public ResponseVO delete(@PathVariable("key") String key) {
        try {
            Pair<Boolean, Object> result = setService.delete(key);
            if (result.getFirst()) {
                return ResponseVO.SUCCESS(result.getSecond());
            } else {
                return ResponseVO.FAIL("fail");
            }
        } catch (Exception e) {
            log.error("method:delete 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @GetMapping("findOne/{key}")
    @ApiOperation(value = "查询单个set")
    public ResponseVO findOne(@PathVariable("key") String key) {
        try {
            return ResponseVO.SUCCESS(setService.findOne(key));
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("union")
    @ApiOperation(value = "并集set")
    public ResponseVO union(@RequestBody SetComparatorVO setUnionVO) {
        try {
            return ResponseVO.SUCCESS(setService.union(setUnionVO.getKey1(), setUnionVO.getKey2()));
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("inter")
    @ApiOperation(value = "交集set")
    public ResponseVO inter(@RequestBody SetComparatorVO setInterVO) {
        try {
            return ResponseVO.SUCCESS(setService.inter(setInterVO.getKey1(), setInterVO.getKey2()));
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("diff")
    @ApiOperation(value = "差集set")
    public ResponseVO diff(@RequestBody SetComparatorVO setDiffVO) {
        try {
            return ResponseVO.SUCCESS(setService.diff(setDiffVO.getKey1(), setDiffVO.getKey2()));
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
