package com.web.controller;

import com.web.service.DeviceService;
import com.web.vo.common.ResponseVO;
import com.web.vo.device.DeviceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/device")
@Api(tags = "设备管理")
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("load")
    @ApiOperation(value = "导入设备数据")
    public ResponseVO load(@RequestParam MultipartFile file) {
        try {
            List<DeviceVO> deviceVOs = deviceService.load(file);
            return ResponseVO.SUCCESS(deviceVOs);
        } catch (Exception e) {
            log.error("method:add 异常", e);
            return ResponseVO.FAIL(e.getMessage());
        }
    }
}
