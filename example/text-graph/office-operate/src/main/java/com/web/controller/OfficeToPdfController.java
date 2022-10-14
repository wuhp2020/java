package com.web.controller;

import com.web.service.OfficeToPdfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/v1/officetopdf")
@Api(tags = "office文件转pdf")
@Slf4j
public class OfficeToPdfController {

    @Resource
    private OfficeToPdfService officeToPdfService;

    @GetMapping("/preview")
    @ApiOperation(value = "预览: linux 需要 yum install libreoffice")
    public void preview(HttpServletResponse response, @RequestParam("fileName") String fileName) throws Exception {
        InputStream inputStream = new FileInputStream("/home/" + fileName);

        // 输出格式
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + fileName);

        // 获取本地文件或网络文件
        OutputStream outputStream = response.getOutputStream();

        officeToPdfService.officeToPdf(inputStream, outputStream, fileName);

        inputStream.close();
        outputStream.close();
    }
}
