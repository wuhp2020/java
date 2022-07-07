package com.web.vo.image;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Data
public class ImageVO {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "图片ID")
    private String imageId;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "图片base64")
    private String imagebase64;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
