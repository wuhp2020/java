package com.web.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BIOThreadPoolLongLinkServer {

    // 阻塞IO+多线程+长连接
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
        ServerSocket serverSocket = new ServerSocket(8088);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            executor.submit(new BIOThreadPoolLongLinkRequestHandler(clientSocket));
        }
    }
}

@Slf4j
class BIOThreadPoolLongLinkRequestHandler implements Runnable {

    private Socket clientSocket;
    public BIOThreadPoolLongLinkRequestHandler(Socket socket) {
        clientSocket = socket;
    }

    @Override
    public void run() {
        byte[] b = new byte[512];
        try {
            while (true) {
                InputStream in = clientSocket.getInputStream();
                int len;
                StringBuffer sb = new StringBuffer();
                while ((len = in.read(b)) != -1) {
                    sb.append(new String(b, 0, len));
                }
                log.info("server get: " + sb.toString());
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
