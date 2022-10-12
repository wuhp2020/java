package com.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@Api(tags = "excel文件")
@Slf4j
public class XlsController {

    @GetMapping("/xls")
    @ApiOperation(value = "预览")
    public void view(HttpServletResponse response) throws Exception {

        // 输出格式
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=a.pdf");

        // 获取本地文件或网络文件
        InputStream inputStream = new FileInputStream("/Users/wuheping/Desktop/1.xlsx");
        OutputStream outputStream = response.getOutputStream();

        inputStream.close();
        outputStream.close();
    }
}
