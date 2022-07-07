package com.web.service;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BIOSingleThreadShortLinkServer {

    // 阻塞IO+单线程+短连接
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8088);
        byte[] b = new byte[512];
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                InputStream in = clientSocket.getInputStream();
                OutputStream os = clientSocket.getOutputStream();
                int len;
                StringBuffer sb = new StringBuffer();
                while ((len = in.read(b)) != -1) {
                    sb.append(new String(b, 0, len));
                }
                Thread.sleep(1000);
                log.info("server get: " + sb.toString());
                log.info("server set: "+ sb.toString() + " ok");

                os.write((sb.toString() + " ok").getBytes(StandardCharsets.UTF_8));
                os.flush();
                clientSocket.shutdownOutput();
            } finally {
                clientSocket.close();
            }
        }
    }
}
