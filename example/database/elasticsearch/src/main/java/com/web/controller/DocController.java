package com.web.controller;

import com.web.service.DocService;
import com.web.vo.common.ResponseVO;
import com.web.vo.doc.DocAddVO;
import com.web.vo.doc.DocVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/doc")
@Api(tags = "数据管理")
@Slf4j
public class DocController {

    @Autowired
    private DocService docService;

    @ApiOperation(value = "创建文章索引")
    @PostMapping("index/create")
    public void createIndex() throws Exception {
        docService.createIndex();
    }

    @ApiOperation(value = "添加文章")
    @PostMapping("add")
    public void addDoc(@RequestBody DocAddVO docAddVO) throws Exception {
        docService.addDoc(docAddVO);
    }

    @ApiOperation(value = "根据关键字模糊搜索文章")
    @GetMapping("keywords/search/{keywords}")
    public List<DocVO> searchByKeyWords(@PathVariable("keywords") String keyWords) throws Exception {
        List<DocVO> docVOs = docService.searchByKeyWords(keyWords);
        return docVOs;
    }

    @ApiOperation(value = "按文章类型精确搜索文章")
    @GetMapping("types/search/{types}")
    public List<DocVO> searchByTypes(@PathVariable("types") List<String> types) throws Exception {
        List<DocVO> docVOS = docService.searchByTypes(types);
        return docVOS;
    }

    @ApiOperation(value = "删除文章")
    @DeleteMapping("delete/{ids}")
    public void deleteDocs(@PathVariable("ids") List<String> ids) throws Exception {
        docService.deleteDocs(ids);
    }
}
