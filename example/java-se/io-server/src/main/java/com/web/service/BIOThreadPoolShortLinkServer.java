package com.web.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BIOThreadPoolShortLinkServer {

    // 阻塞IO+多线程+短连接
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 30,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
        ServerSocket serverSocket = new ServerSocket(8088);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            executor.submit(new BIOThreadPoolShortLinkRequestHandler(clientSocket));
        }
    }
}

@Slf4j
class BIOThreadPoolShortLinkRequestHandler implements Runnable {

    private Socket clientSocket;
    public BIOThreadPoolShortLinkRequestHandler(Socket socket) {
        clientSocket = socket;
    }

    @Override
    public void run() {
        byte[] b = new byte[512];
        try {
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
