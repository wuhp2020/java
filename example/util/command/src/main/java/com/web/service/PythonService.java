package com.web.service;

import com.spring.config.PythonCommandProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
@Slf4j
public class PythonService {

    @Autowired
    private PythonCommandProperties commandProperties;

    public void execPrint() throws Exception {
        List<String> commands = commandProperties.getCommands();

        ProcessBuilder builder = new ProcessBuilder(commands);
        Process process = builder.start();

        new Thread() {
            public void run() {
                BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
                try {
                    String logLine;
                    while ((logLine = stdout.readLine()) != null) {
                        if (logLine != null) {
                            log.info(logLine);
                        }
                    }
                } catch (Exception e) {
                    log.error("错误", e);
                }
                finally{
                    try {
                        stdout.close();
                    } catch (Exception e) {
                        log.error("错误", e);
                    }
                }
            }
        }.start();

        new Thread() {
            public void  run() {
                BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                try {
                    String logLine;
                    while ((logLine = stderr.readLine()) != null) {
                        if (logLine != null) {
                            log.info("========" + logLine);
                        }
                    }
                } catch (Exception e) {
                    log.error("错误", e);
                }
                finally{
                    try {
                        stderr.close();
                    } catch (Exception e) {
                        log.error("错误", e);
                    }
                }
            }
        }.start();

        // 等待程序运行结束
        int status = process.waitFor();
        process.destroy();
        log.info("status: " + status);
        if (status != 0) {
            throw new Exception("工具调用失败");
        }
    }
}
