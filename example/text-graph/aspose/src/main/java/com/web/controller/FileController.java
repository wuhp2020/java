package com.web.controller;

import com.web.util.Office2PDFUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api/v1/file")
@Api(tags = "文件")
@Slf4j
public class FileController {

    @GetMapping("/view")
    @ApiOperation(value = "预览")
    public void view(HttpServletResponse response) throws Exception {
        String netFileUrl = "https://study-dfs.sugon.com/group1/M00/00/09/CgJoA2ND5iuASj5AAJ9K7L435CQ85.pptx";
        URL url = new URL(netFileUrl);
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
        httpUrl.connect();

        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=a.pdf");

        InputStream inputStream = httpUrl.getInputStream();
        OutputStream outputStream = response.getOutputStream();
        if (netFileUrl.endsWith(".docx") || netFileUrl.endsWith(".doc")) {
            Office2PDFUtil.doc2pdf(inputStream, outputStream);
        } else if (netFileUrl.endsWith(".xlsx") || netFileUrl.endsWith(".xls")) {
            Office2PDFUtil.excel2pdf(inputStream, outputStream);
        } else if (netFileUrl.endsWith(".pptx") || netFileUrl.endsWith(".ppt")) {
            Office2PDFUtil.ppt2pdf(inputStream, outputStream);
        }
        httpUrl.disconnect();
        inputStream.close();
        outputStream.close();
    }
}
