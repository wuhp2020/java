package com.web.controller;

import com.google.common.collect.Lists;
import com.web.vo.common.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ Author : wuheping
 * @ Date   : 2022/3/7
 * @ Desc   : 描述
 */
@RestController
@RequestMapping("/api/v1/optional")
@Api(tags = "空指针管理")
@Slf4j
public class OptionalController {

    @ApiOperation(value = "避免空指针1")
    @PostMapping("opt1")
    public void opt1() {
        List<String> list1 = null;
        List<String> list2 = Lists.newArrayList("1", "2", "3", "4");
        Optional.ofNullable(list1).orElseGet(() -> list2).stream().filter(s -> "1".equals(s)).forEach(s -> log.info(s));
        Optional.ofNullable(list1).orElse(list2).stream().filter(s -> "2".equals(s)).forEach(s -> log.info(s));
        Optional.ofNullable(list1).ifPresent(list -> list.stream().filter(s -> "3".equals(s)).forEach(s -> log.info(s)));
        Optional.ofNullable(list2).ifPresent(list -> list.stream().filter(s -> "4".equals(s)).forEach(s -> log.info(s)));
    }

    @ApiOperation(value = "避免空指针2")
    @PostMapping("opt2")
    public void opt2() {
        ResponseVO<List<String>> responseVO = null;
        Optional.ofNullable(responseVO)
                .flatMap(res -> Optional.ofNullable(res.getData()))
                .orElse(Collections.emptyList()).stream().forEach(s -> log.info(s));
    }

    @ApiOperation(value = "避免空指针3")
    @PostMapping("opt3")
    public void opt3() {
        List<String> list = Lists.newArrayList("1", "2", "3", "4");
        ResponseVO<List<String>> responseVO = new ResponseVO("200", "ok", list);
        Optional.ofNullable(responseVO)
                .flatMap(res -> Optional.ofNullable(res.getData()))
                .orElse(Collections.emptyList()).stream().forEach(s -> log.info(s));
    }

    @ApiOperation(value = "避免空指针4")
    @PostMapping("opt4")
    public void opt4() {
        List<String> list = Lists.newArrayList("1", "2", "3", "4");
        ResponseVO<List<String>> responseVO = new ResponseVO("200", "ok", list);
        Optional.ofNullable(responseVO)
                .flatMap(res -> Optional.ofNullable(res.getData()))
                .ifPresent(ls -> {
                    List<String> ss = ls.stream().filter(s -> "1".equals(s)).collect(Collectors.toList());
                    Optional.ofNullable(ss).orElse(Collections.emptyList()).stream().forEach(s -> log.info(s));
                });
    }
}
