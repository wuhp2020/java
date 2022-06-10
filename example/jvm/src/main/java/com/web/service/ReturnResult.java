package com.web.service;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/11
 * @ Desc   : 描述
 */
public class ReturnResult {

    public static void main(String[] args) {
        System.out.println(ReturnResult.a());
//        System.out.println(ReturnResult.b());
    }

    // 调用打印结果
    // try: 2
    // finally: 3
    // 2
    public static int a() {
        int i = 1;
        try {
            i++;
            System.out.println("try: " + i);
            return i;
        } catch (Exception e) {
            i++;
            System.out.println("catch: " + i);
        } finally {
            i++;
            System.out.println("finally: " + i);
        }
        return i;
    }

    // 调用打印结果
    // try: 2
    // finally: 3
    // 3
    public static int b() {
        int i = 1;
        try {
            i++;
            System.out.println("try: " + i);
            return i;
        } catch (Exception e) {
            i++;
            System.out.println("catch: " + i);
        } finally {
            i++;
            System.out.println("finally: " + i);
            return i;
        }
    }
}
