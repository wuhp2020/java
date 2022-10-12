package com.web.controller;

import com.spire.presentation.FileFormat;
import com.spire.presentation.Presentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/v1/ppt")
@Api(tags = "ppt文件")
@Slf4j
public class PptController {

    @GetMapping("/view")
    @ApiOperation(value = "预览")
    public void view(HttpServletResponse response) throws Exception {

        // 输出格式
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=a.pdf");

        // 获取本地文件或网络文件
        InputStream inputStream = new FileInputStream("/Users/wuheping/Desktop/1.pptx");
        OutputStream outputStream = response.getOutputStream();

        // ppt或pptx转pdf
        Presentation ppt = new Presentation();
        ppt.loadFromStream(inputStream, FileFormat.PPTX_2013);
        ppt.saveToFile(outputStream, FileFormat.PDF);
        ppt.dispose();

        inputStream.close();
        outputStream.close();
    }
}
