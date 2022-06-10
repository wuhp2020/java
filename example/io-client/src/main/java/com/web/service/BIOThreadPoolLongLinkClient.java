package com.web.service;

import lombok.extern.slf4j.Slf4j;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BIOThreadPoolLongLinkClient {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(new BIOThreadPoolLongLinkRequest(i)).start();
        }
    }
}

@Slf4j
class BIOThreadPoolLongLinkRequest implements Runnable {
    private int index;

    public BIOThreadPoolLongLinkRequest(int index) {
        this.index = index;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 8088);
            for (int i = 0; i < 30; i++) {
                OutputStream os = socket.getOutputStream();
                os.write((index + i + "").getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

