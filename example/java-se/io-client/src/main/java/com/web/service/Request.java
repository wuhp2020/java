package com.web.service;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Request implements Runnable {
    private int index;

    public Request(int index) {
        this.index = index;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 8088);
            OutputStream os = socket.getOutputStream();
            os.write((index + "").getBytes(StandardCharsets.UTF_8));
            os.flush();
            log.info("client set: " + index);
            // 不shutdown的话对server会等待read
            socket.shutdownOutput();

            InputStream in = socket.getInputStream();
            StringBuffer sb = new StringBuffer();
            byte[] b = new byte[512];
            int len;
            while ((len = in.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            log.info("client get: " + sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
