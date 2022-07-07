package com.web.service;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/12
 * @ Desc   : 描述
 */
public class VolatileService {

    static int x = 0;
    static boolean ready = false;

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ready) {
                    System.out.println(Thread.currentThread().getId() + " -> " + (x + x));
                    if ((x + x) == 0) {
                        System.out.println(Thread.currentThread().getId() + " -> 指令重排");
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                x = 1;
                ready = true;
            }
        }).start();
    }
}
