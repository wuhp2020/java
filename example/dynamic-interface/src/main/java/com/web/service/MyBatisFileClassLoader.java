package com.web.service;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import static jdk.internal.org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static jdk.internal.org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static jdk.internal.org.objectweb.asm.Opcodes.ACC_PUBLIC;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/1
 * @ Desc   : 描述
 */
public class MyBatisFileClassLoader extends ClassLoader {

    private static byte[] dump() {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(52, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, "com/BasicMapper", null, "java/lang/Object", null);
        cw.visitSource("BasicMapper.java", null);
        {
            MethodVisitor mvSelect = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "select", "(Ljava/util/Map;)Ljava/util/List;", "(Ljava/util/Map;)Ljava/util/List<Ljava/lang/Object;>;", null);
            mvSelect.visitEnd();
            MethodVisitor mvInsert = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "insert", "(Ljava/util/Map;)V", null, null);
            mvInsert.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    public Class<?> defineClass(String name) throws Exception {
        // ClassLoader是个抽象类, 而ClassLoader.defineClass 方法是protected的
        // 所以我们需要定义一个子类将这个方法暴露出来
        Class<?> clazz = super.findLoadedClass(name);
        if (clazz != null) {
            return clazz;
        }
        byte[] b = MyBatisFileClassLoader.dump();
        return super.defineClass(name, b, 0, b.length);
    }
}
