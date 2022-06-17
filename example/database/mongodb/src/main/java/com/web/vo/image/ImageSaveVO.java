package com.web.vo.image;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImageSaveVO {

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "图片base64")
    private String imagebase64;

}
