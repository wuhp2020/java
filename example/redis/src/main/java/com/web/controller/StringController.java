package com.web.controller;

import com.web.service.StringService;
import com.web.vo.common.ResponseVO;
import com.web.vo.string.StringSaveVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/string")
@Api(tags = "string类型")
@Slf4j
public class StringController {

    @Autowired
    private StringService stringService;

    @PostMapping("save")
    @ApiOperation(value = "增加string")
    public void save(@RequestBody StringSaveVO stringSaveVO) throws Exception {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0;i < 1000000;i++) {
                    log.info(i + "");
                }
            }
        }.start();
        stringService.save(stringSaveVO.getKey(), stringSaveVO.getValue());
    }

    @DeleteMapping("delete/{keys}")
    @ApiOperation(value = "删除string")
    public void delete(@PathVariable("keys") List<String> keys) {
        Pair<Boolean, String> result = stringService.delete(keys);
        if (!result.getFirst()) {
            throw new RuntimeException(result.getSecond());
        }
    }

    @GetMapping("findOne/{key}")
    @ApiOperation(value = "查询单个string")
    public void findOne(@PathVariable("key") String key) throws Exception {
        stringService.findOne(key);
    }

    @PostMapping("increment")
    @ApiOperation(value = "加一")
    public void increment(@RequestBody String key) throws Exception {
        stringService.increment(key);
    }

    @PostMapping("decrement")
    @ApiOperation(value = "减一")
    public void decrement(@RequestBody String key) throws Exception {
        stringService.decrement(key);
    }

    @PostMapping("topic")
    @ApiOperation(value = "发布订阅")
    public void topic(@RequestBody String message) throws Exception {
        stringService.topic(message);
    }

}
