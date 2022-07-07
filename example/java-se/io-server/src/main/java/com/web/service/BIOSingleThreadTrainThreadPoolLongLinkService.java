package com.web.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BIOSingleThreadTrainThreadPoolLongLinkService {

    // 当前的socket
    final public static Set<Socket> socketSet = new HashSet<>(10);

    // 阻塞IO+单线程轮询+多线程处理+长连接
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
        ServerSocket serverSocket = new ServerSocket(8088);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        synchronized (socketSet) {
                            Iterator<Socket> it = socketSet.iterator();
                            while (it.hasNext()) {
                                Socket socket = it.next();
                                if (socket.isConnected()) {
                                    if (!socket.isInputShutdown() && socket.getInputStream().available() > 0) {
                                        // 开始接数据, 放出来
                                        it.remove();
                                        executor.submit(new BIOSingleThreadTrainThreadPoolLongLinkRequestHandler(socket));
                                    }
                                } else {
                                    socketSet.remove(socket);
                                    try {
                                        socket.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }).start();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            synchronized (socketSet) {
                socketSet.add(clientSocket);
            }
        }
    }
}

@Slf4j
class BIOSingleThreadTrainThreadPoolLongLinkRequestHandler implements Runnable {

    private Socket clientSocket;
    public BIOSingleThreadTrainThreadPoolLongLinkRequestHandler(Socket socket) {
        clientSocket = socket;
    }

    @Override
    public void run() {
        byte[] b = new byte[512];
        try {
            InputStream in = clientSocket.getInputStream();
            int len;
            StringBuffer sb = new StringBuffer();
            while ((len = in.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            log.info("server get: " + sb.toString());
            // 接完数据, 放回去
            synchronized (BIOSingleThreadTrainThreadPoolLongLinkService.socketSet) {
                BIOSingleThreadTrainThreadPoolLongLinkService.socketSet.add(clientSocket);
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
