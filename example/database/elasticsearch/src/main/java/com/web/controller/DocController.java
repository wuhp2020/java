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
    public ResponseVO createIndex(){
        try {
            docService.createIndex();
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("创建文章索引失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "添加文章")
    @PostMapping("add")
    public ResponseVO addDoc(@RequestBody DocAddVO docAddVO) {
        try {
            docService.addDoc(docAddVO);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("添加文章失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "根据关键字模糊搜索文章")
    @GetMapping("keywords/search/{keywords}")
    public ResponseVO searchByKeyWords(@PathVariable("keywords") String keyWords){
        try {
            List<DocVO> docVOs = docService.searchByKeyWords(keyWords);
            return ResponseVO.SUCCESS(docVOs);
        } catch (Exception e) {
            log.error("根据关键字模糊搜索文章失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "按文章类型精确搜索文章")
    @GetMapping("types/search/{types}")
    public ResponseVO searchByTypes(@PathVariable("types") List<String> types){
        try {
            List<DocVO> docVOS = docService.searchByTypes(types);
            return ResponseVO.SUCCESS(docVOS);
        } catch (Exception e) {
            log.error("按文章类型精确搜索文章失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @ApiOperation(value = "删除文章")
    @DeleteMapping("delete/{ids}")
    public ResponseVO deleteDocs(@PathVariable("ids") List<String> ids){
        try {
            docService.deleteDocs(ids);
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("删除文章失败", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
