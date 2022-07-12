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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dict")
@Slf4j
@Api(tags = "字典管理")
public class DictController {

    @Autowired
    private DictService dictService;

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询字典")
    public Page<DictVO> findByPage(@RequestBody DictQueryVO dictQueryVO) throws Exception {
        Page<DictVO> page = dictService.findByPage(dictQueryVO);
        return page;
    }

}
