package com.web.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RpcService implements DetectApiService {

    @Override
    public String detect(String base64) {
        return "response";
    }

    // 入口
    public static void main(String[] args) {
        DetectApiService detectApiService = new RpcService();
        RpcServer server = new RpcServer();
        log.info("started rpc service");
        server.register(detectApiService, 8088);
    }
}

interface DetectApiService {
    public String detect(String base64);
}


@Slf4j
class RpcServer {

    private ExecutorService executorService = new ThreadPoolExecutor(5, 5,
            60000, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>());

    public void register(Object service, int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = null;
            while ((socket = serverSocket.accept()) != null) {
                executorService.execute(new Processor(socket, service));
            }
        } catch (IOException e) {
            log.error("method: register() fail", e);
        }
    }
}

@Data
@Slf4j
class Processor implements Runnable {

    private Socket socket;
    private Object service;

    public Processor(Socket socket, Object service){
        this.socket = socket;
        this.service = service;
    }
    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            String methodName = inputStream.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
            Object[] parameters = (Object[]) inputStream.readObject();
            Method method = service.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(service, parameters);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(result);
        } catch (Exception e) {
            log.error("method: execute() fail", e);
        }
    }
}

