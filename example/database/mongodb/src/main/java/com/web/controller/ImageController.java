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
    public ImageVO save(@RequestBody ImageSaveVO imageSaveVO) throws Exception {
        // 保存
        ImageVO imageVO = imageService.save(imageSaveVO);
        return imageVO;
    }

    @DeleteMapping("delete/{imageIds}")
    @ApiOperation(value = "删除字典")
    public void delete(@PathVariable("imageIds") List<String> imageIds) {
        Pair<Boolean, String> result = imageService.delete(imageIds);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond());
        }
    }


    @GetMapping("findOne/{imageId}")
    @ApiOperation(value = "查询单个字典")
    public ImageVO findOne(@PathVariable("imageId") String imageId) throws Exception {
        ImageVO imageVO = imageService.findByImageId(imageId);
        return imageVO;
    }

    @PostMapping("findByPage")
    @ApiOperation(value = "分页查询字典")
    public Page<ImageVO> findByPage(ImageQueryVO imageQueryVO) throws Exception {
        Page<ImageVO> page = imageService.findByPage(imageQueryVO);
        return page;
    }

}
