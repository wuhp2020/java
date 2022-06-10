package com.web.controller;

import com.web.service.StreamService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stream")
@Api(tags = "数据流")
@Slf4j
public class StreamController {

    @Autowired
    private StreamService streamService;
}
