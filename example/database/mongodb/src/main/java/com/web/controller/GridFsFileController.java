package com.web.controller;

import com.web.service.GridFsFileService;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gridfsfile")
@Api(tags = "图片存储管理")
@Slf4j
public class GridFsFileController {

    @Autowired
    private GridFsFileService gridFsFileService;

    @PostMapping("save")
    @ApiOperation(value = "增加图片")
    public void save(@RequestBody String imagebase64) {
        // 保存
        gridFsFileService.save(imagebase64);
    }

    @DeleteMapping("delete/{imageIds}")
    @ApiOperation(value = "删除图片")
    public void delete(@PathVariable("imageIds") List<String> imageIds) {
        gridFsFileService.delete(imageIds);
    }


    @GetMapping("findOne/{imageId}")
    @ApiOperation(value = "查询单个图片")
    public void findOne(@PathVariable("imageId") String imageId) throws Exception {
        String imageBase64 = gridFsFileService.get(imageId);
    }
}
