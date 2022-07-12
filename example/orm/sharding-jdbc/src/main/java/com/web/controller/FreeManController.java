package com.web.controller;

import com.web.service.FreeManService;
import com.web.vo.common.ResponseVO;
import com.web.vo.freeman.FreeManBetchAddVO;
import com.web.vo.freeman.FreeManQueryVO;
import com.web.vo.freeman.FreeManVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/freeman")
@Api(tags = "中国公民管理")
@Slf4j
public class FreeManController {

    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(4, 4,
            10, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    @Autowired
    private FreeManService freemanService;

    @ApiOperation(value = "批量添加用户")
    @PostMapping("batchAdd")
    public void batchAddUser(@RequestBody FreeManBetchAddVO freeManBetchAddVO) {
        for (long i = freeManBetchAddVO.getBeginId(); i < freeManBetchAddVO.getEndId(); i++) {
            log.info("执行到: " + i);
            final long j = i;
            poolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        freemanService.addFreeMan(j);
                    } catch (Exception e) {
                        log.error("添加失败", e);
                    }
                }
            });
        }
    }

    @ApiOperation(value = "身份证")
    @PostMapping("findByIdentity")
    public FreeManVO findByIdentity(@RequestBody FreeManQueryVO freeManQueryVO) throws Exception {
        FreeManVO freeManVO = freemanService.findByIdentity(freeManQueryVO);
        return freeManVO;
    }
}
