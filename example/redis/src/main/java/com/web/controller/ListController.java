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


@RestController
@RequestMapping("/api/v1/list")
@Api(tags = "list类型")
@Slf4j
public class ListController {
    @Autowired
    private ListService listService;

    @PostMapping("save")
    @ApiOperation(value = "增加list")
    public ResponseVO save(@RequestBody ListSaveVO listSaveVO) {
        try {
            listService.save(listSaveVO.getKey(), listSaveVO.getValue());
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @DeleteMapping("delete/{key}")
    @ApiOperation(value = "删除list")
    public ResponseVO delete(@PathVariable("key") String key) {
        try {
            Pair<Boolean, Object> result = listService.delete(key);
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
    @ApiOperation(value = "查询单个list")
    public ResponseVO findOne(@PathVariable("key") String key) {
        try {
            return ResponseVO.SUCCESS(listService.findAll(key));
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
