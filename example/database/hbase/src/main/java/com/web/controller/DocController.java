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


}
