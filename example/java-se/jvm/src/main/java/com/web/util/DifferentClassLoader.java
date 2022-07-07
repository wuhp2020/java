package com.web.util;

import java.io.IOException;
import java.io.InputStream;

public class DifferentClassLoader {
    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                InputStream stream = getClass().getResourceAsStream(fileName);
                if (stream == null) {
                    return super.loadClass(name);
                }
                try {
                    byte[] b = new byte[stream.available()];
                    // 将流写入字节数组b中
                    stream.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return super.loadClass(name);
            }
        };

        Object obj = classLoader.loadClass("com.web.util.DifferentClassLoader").newInstance();
        System.out.println(obj.getClass());
        System.out.println(obj instanceof DifferentClassLoader);
        // 强制转化报错, 因为类加载不是一个
        DifferentClassLoader obj1 = (DifferentClassLoader) classLoader
                .loadClass("com.web.util.DifferentClassLoader").newInstance();

    }
}
