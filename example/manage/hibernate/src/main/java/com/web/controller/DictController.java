package com.web.controller;

import com.web.service.DictService;
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
    public void save(DictVO dictVO) throws Exception {
        // 校验
        Pair<Boolean, String> result = dictService.checkRepeat(dictVO);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond());
        }
        // 保存
        dictVO = dictService.save(dictVO);
    }

    @DeleteMapping("delete/{ids}")
    @ApiOperation(value = "删除字典")
    public void delete(@PathVariable("ids") List<String> ids) {
        Pair<Boolean, String> result = dictService.delete(ids);
        if (result.getFirst()) {
            throw new RuntimeException(result.getSecond());
        }
    }

    @PutMapping("update")
    @ApiOperation(value = "修改字典")
    public void update(DictVO dictVO) throws Exception {
        dictVO = dictService.update(dictVO);
    }

    @GetMapping("findOne/{id}")
    @ApiOperation(value = "查询单个字典")
    public DictVO findOne(@PathVariable("id") String id) throws Exception {
        DictVO dictVO = dictService.findOne(id);
        return dictVO;
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询字典")
    public Page<DictVO> findByPage(DictQueryVO dictQueryVO) throws Exception {
        Page<DictVO> page = dictService.findByPage(dictQueryVO);
        return page;
    }

}
