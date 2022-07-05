package com.web.util;

import io.xjar.XCryptos;

/**
 * @ Author     : wuhp
 * @ Date       : 2022/1/21
 * @ Description: 描述
 */
public class XjarUtil {

    public static void main(String[] args) throws Exception {
        // Spring-Boot Jar包加密
        XCryptos.encryption()
                // 项目生成的jar
                .from("/Users/mac/Desktop/swagger.jar")
                // 加密的密码
                .use("io.xjar")
                // 要加密的资源
                .exclude("/static/**/*")
                .exclude("/templates/**/*")
                .exclude("/META-INF/resources/**/*")
                // 加密后的jar
                .to("/Users/mac/Desktop/swagger-x.jar");
        System.out.println("========== success ==========");
        System.out.println("后续步骤:");
        System.out.println("1. 需要根据运行的系统环境编译: go build xjar.go");
        System.out.println("2. 编译后在当前目录下生成文件: xjar或xjar.exe");
        System.out.println("3. 运行命令: ./xjar java -jar swagger-x.jar");
        System.out.println("4. 服务启动成功");
    }

}
