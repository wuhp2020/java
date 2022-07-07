package com.web.service;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

@Slf4j
public class NIOSingleThreadTrainThreadPoolLongLinkService {

    public static void main(String[] args) throws Exception{
        //1. 创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        //2. 创建Selector
        Selector selector = Selector.open();
        try {
            socketChannel.configureBlocking(false);
            //3. 关联SocketChannel和Socket, socket绑定到本机端口
            socketChannel.socket().bind(new InetSocketAddress(8088));
            //4. 注册到Selector, 感兴趣的事件为OP_CONNECT、OP_READ、OP_WRITE
            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            int i = 0;
            boolean written = false, done = false;
            String encoding = System.getProperty("file.encoding");
            Charset cs = Charset.forName(encoding);
            ByteBuffer buffer = ByteBuffer.allocate(16);
            while(!done) {
                selector.select();
                //5. 从选择器中获取所有注册的通道信息（SelectionKey作为标识）
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    //6. 获取通道, 此处即为上边创建的channel
                    socketChannel = (SocketChannel)key.channel();
                    //7. 判断SelectorKey对应的channel发生的事件是否socket连接, 并且还没有连接
                    if(key.isConnectable() && !socketChannel.isConnected()) {
                        InetAddress addr = InetAddress.getByName(null);
                        // 连接addr和port对应的服务器
                        boolean success = socketChannel.connect(new InetSocketAddress(8088));
                        if(!success) {
                            socketChannel.finishConnect();
                        }
                    }
                    //8. 读与写是非阻塞的, 客户端写一个信息到服务器, 服务器发送一个信息到客户端, 客户端再读
                    if(key.isReadable() && written) {
                        if(socketChannel.read((ByteBuffer)buffer.clear()) > 0) {
                            written = false;
                            String response = cs.decode((ByteBuffer)buffer.flip()).toString();
                            System.out.println(response);
                            if(response.indexOf("END") != -1) {
                                done = true;
                            }
                        }
                    }
                    if(key.isWritable() && !written) {
                        if(i < 10) {
                            socketChannel.write(ByteBuffer.wrap(new String("howdy " + i + "\n").getBytes()));
                        } else if(i == 10){
                            socketChannel.write(ByteBuffer.wrap("END".getBytes()));
                        }
                        written = true;
                        i++;
                    }
                }
            }
        } finally {
            socketChannel.close();
            selector.close();
        }
    }
}