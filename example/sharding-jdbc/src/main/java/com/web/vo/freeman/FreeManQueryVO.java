package com.web.vo.freeman;

import com.web.vo.common.PageQueryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FreeManQueryVO {

    @ApiModelProperty(value = "身份证")
    private String identity;
}
