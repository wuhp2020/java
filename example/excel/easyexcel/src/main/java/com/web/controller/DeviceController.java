package com.web.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
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

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @PostMapping("export")
    @ApiOperation(value = "导出设备数据")
    public void export(HttpServletResponse response) {
        try {
            List<DeviceVO> deviceVOS = deviceService.export();

            OutputStream out = response.getOutputStream();
            ExcelWriter excelWriter = new ExcelWriter(out, ExcelTypeEnum.XLSX);;
            response.setHeader("Content-type", "application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
            response.setHeader("Content-Disposition", "attachment;filename=" + sdf.format(new Date()) + ".xlsx");
            Sheet sheet = new Sheet(0);
            sheet.setSheetName("sheet");
            sheet.setClazz(DeviceVO.class);
            excelWriter.write(deviceVOS,sheet);
            excelWriter.finish();
            out.flush();
        } catch (Exception e) {
            log.error("method:add 异常", e);
        }
    }
}
