package com.web.controller;

import com.itextpdf.text.Document;
import com.web.util.DocUtil;
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
@RequestMapping("/api/v1/doc")
@Api(tags = "文件")
@Slf4j
public class DocController {

    @GetMapping("/view")
    @ApiOperation(value = "预览")
    public void view(HttpServletResponse response) throws Exception {

        // 输出格式
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=a.pdf");

        // 获取本地文件或网络文件
        InputStream inputStream = new FileInputStream("/Users/wuheping/Desktop/1.docx");
        OutputStream outputStream = response.getOutputStream();

    }
}
