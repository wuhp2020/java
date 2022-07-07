package com.web.service;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/9
 * @ Desc   : 描述
 */
public class StackErrorService {
    private static int index = 1;

    public void call() {
        index++;
        call();
    }

    public static void main(String[] args) {
        StackErrorService mock = new StackErrorService();
        try {
            mock.call();
        } catch(Throwable e) {
            System.out.println("Stack deep: " + index);
            e.printStackTrace();
        }
    }
}
