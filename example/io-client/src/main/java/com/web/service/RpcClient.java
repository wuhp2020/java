package com.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

@Service
@Slf4j
public class RpcClient {

    public static void main(String[] args) {
        DetectApiService detectApiService = RpcClient.getClient(DetectApiService.class, "127.0.0.1", 8088);
        String result = detectApiService.detect("request");
        log.info(result);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getClient(Class<T> clazz, String ip, int port) {
        return (T) Proxy.newProxyInstance(RpcClient.class.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
                Socket socket = new Socket(ip, port);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeUTF(method.getName());
                outputStream.writeObject(method.getParameterTypes());
                outputStream.writeObject(objects);
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                return inputStream.readObject();
            }
        });
    }
}

interface DetectApiService {
    public String detect(String base64);
}

