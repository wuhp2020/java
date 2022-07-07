package com.web.controller;

import com.web.service.ImageService;
import com.web.vo.common.ResponseVO;
import com.web.vo.image.ImageQueryVO;
import com.web.vo.image.ImageSaveVO;
import com.web.vo.image.ImageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/image")
@Api(tags = "图片管理")
@Slf4j
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("save")
    @ApiOperation(value = "增加图片")
    public ResponseVO save(@RequestBody ImageSaveVO imageSaveVO) {
        try {
            // 保存
            ImageVO imageVO = imageService.save(imageSaveVO);
            return ResponseVO.SUCCESS(imageVO);
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @DeleteMapping("delete/{imageIds}")
    @ApiOperation(value = "删除字典")
    public ResponseVO delete(@PathVariable("imageIds") List<String> imageIds) {
        try {
            Pair<Boolean, String> result = imageService.delete(imageIds);
            if (result.getFirst()) {
                return ResponseVO.SUCCESS(result.getSecond());
            } else {
                return ResponseVO.FAIL(result.getSecond());
            }
        } catch (Exception e) {
            log.error("method:delete 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }


    @GetMapping("findOne/{imageId}")
    @ApiOperation(value = "查询单个字典")
    public ResponseVO findOne(@PathVariable("imageId") String imageId) {
        try {
            ImageVO imageVO = imageService.findByImageId(imageId);
            return ResponseVO.SUCCESS(imageVO);
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询字典")
    public ResponseVO findByPage(ImageQueryVO imageQueryVO) {
        try {
            Page<ImageVO> page = imageService.findByPage(imageQueryVO);
            return ResponseVO.SUCCESS(page);
        } catch (Exception e) {
            log.error("method:findByPage 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
