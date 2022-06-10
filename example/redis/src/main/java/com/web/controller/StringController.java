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
    public ResponseVO save(@RequestBody StringSaveVO stringSaveVO) {
        try {
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0;i < 1000000;i++) {
                        log.info(i + "");
                    }
                }
            }.start();
            stringService.save(stringSaveVO.getKey(), stringSaveVO.getValue());
            return ResponseVO.SUCCESS(null);
        } catch (Exception e) {
            log.error("method:save 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @DeleteMapping("delete/{keys}")
    @ApiOperation(value = "删除string")
    public ResponseVO delete(@PathVariable("keys") List<String> keys) {
        try {
            Pair<Boolean, String> result = stringService.delete(keys);
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

    @GetMapping("findOne/{key}")
    @ApiOperation(value = "查询单个string")
    public ResponseVO findOne(@PathVariable("key") String key) {
        try {
            return ResponseVO.SUCCESS(stringService.findOne(key));
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("increment")
    @ApiOperation(value = "加一")
    public ResponseVO increment(@RequestBody String key) {
        try {
            return ResponseVO.SUCCESS(stringService.increment(key));
        } catch (Exception e) {
            log.error("method:increment 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("decrement")
    @ApiOperation(value = "减一")
    public ResponseVO decrement(@RequestBody String key) {
        try {
            return ResponseVO.SUCCESS(stringService.decrement(key));
        } catch (Exception e) {
            log.error("method:decrement 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

    @PostMapping("topic")
    @ApiOperation(value = "发布订阅")
    public ResponseVO topic(@RequestBody String message) {
        try {
            stringService.topic(message);
            return ResponseVO.SUCCESS("ok");
        } catch (Exception e) {
            log.error("method:decrement 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }

}
