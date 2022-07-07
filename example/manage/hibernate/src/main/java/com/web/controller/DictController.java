package com.web.controller;

import com.web.service.DictService;
import com.web.vo.common.ResponseVO;
import com.web.vo.dict.DictQueryVO;
import com.web.vo.dict.DictVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dict")
@Slf4j
@Api(tags = "字典管理")
public class DictController {

    @Autowired
    private DictService dictService;

    @PostMapping("save")
    @ApiOperation(value = "增加字典")
    public ResponseVO save(DictVO dictVO) {
        try {
            // 校验
            Pair<Boolean, String> result = dictService.checkRepeat(dictVO);
            if (!result.getFirst()) {
                return ResponseVO.FAIL(result.getSecond());
            }
            // 保存
            dictVO = dictService.save(dictVO);
            return ResponseVO.SUCCESS(dictVO);
        } catch (Exception e) {
            log.error("class:DictController, method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @DeleteMapping("delete/{ids}")
    @ApiOperation(value = "删除字典")
    public ResponseVO delete(@PathVariable("ids") List<String> ids) {
        try {
            Pair<Boolean, String> result = dictService.delete(ids);
            if (result.getFirst()) {
                return ResponseVO.SUCCESS(result.getSecond());
            } else {
                return ResponseVO.FAIL(result.getSecond());
            }
        } catch (Exception e) {
            log.error("class:DictController, method:delete 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PutMapping("update")
    @ApiOperation(value = "修改字典")
    public ResponseVO update(DictVO dictVO) {
        try {
            dictVO = dictService.update(dictVO);
            return ResponseVO.SUCCESS(dictVO);
        } catch (Exception e) {
            log.error("class:DictController, method:update 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @GetMapping("findOne/{id}")
    @ApiOperation(value = "查询单个字典")
    public ResponseVO findOne(@PathVariable("id") String id) {
        try {
            DictVO dictVO = dictService.findOne(id);
            return ResponseVO.SUCCESS(dictVO);
        } catch (Exception e) {
            log.error("class:DictController, method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询字典")
    public ResponseVO findByPage(DictQueryVO dictQueryVO) {
        try {
            Page<DictVO> page = dictService.findByPage(dictQueryVO);
            return ResponseVO.SUCCESS(page);
        } catch (Exception e) {
            log.error("class:DictController, method:findByPage 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
