package com.web.controller;

import com.web.util.FreemarkerUtil;
import com.web.vo.common.ResponseVO;
import com.web.vo.xml.InterfaceParamVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/xml")
@Api(tags = "管理")
@Slf4j
public class XmlController {

    @PostMapping("select")
    @ApiOperation(value = "生成select的xml")
    public ResponseVO select(InterfaceParamVO paramVO) throws Exception {
        return ResponseVO.SUCCESS(FreemarkerUtil.xml("select.ftl", paramVO));
    }

}
