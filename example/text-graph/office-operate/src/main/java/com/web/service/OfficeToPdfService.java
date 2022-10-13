package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OfficeToPdfService {

    @Value("${tempfilePath}")
    private String tempfilePath;

    public void officeToPdf(InputStream inputStream, OutputStream outputStream, String fileName) throws Exception {
        String uuid = UUID.randomUUID().toString();

        // 创建临时文件
        List<String> createCommands = new LinkedList<>();
        createCommands.add("mkdir -p "+ tempfilePath +"/" + uuid);
        this.execShell(createCommands);
        byte[] buffer = new byte[102400];
        int len = 0;
        FileOutputStream fos = new FileOutputStream(new File(tempfilePath +"/"+ uuid + "/" + fileName));
        while ((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.close();

        // 生成pdf
        List<String> convertCommands = new LinkedList<>();
        convertCommands.add("cd "+ tempfilePath +"/"+ uuid + " && libreoffice --invisible --convert-to pdf " + fileName);
        this.execShell(convertCommands);

        // 写出pdf
        String pdfFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf";
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(tempfilePath +"/"+ uuid + "/" + pdfFileName));
        while ((len = br.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        br.close();

        // 删除临时文件
        List<String> deleteCommands = new LinkedList<>();
        deleteCommands.add("rm -rf " + tempfilePath +"/"+ uuid);
        this.execShell(deleteCommands);
    }

    private void execShell(List<String> commands) throws Exception {
        Runtime run = Runtime.getRuntime();
        Process proc = run.exec("/bin/bash", null, null);
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
        for (String line : commands) {
            out.println(line);
        }
        // 这个命令必须执行, 否则in流不结束
        out.println("exit");
        String rspLine = "";
        while ((rspLine = in.readLine()) != null) {
            log.info(rspLine);
        }
        proc.waitFor();
        in.close();
        out.close();
        proc.destroy();
    }
}
