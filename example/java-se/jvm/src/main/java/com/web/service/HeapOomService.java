package com.web.service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/9
 * @ Desc   : 描述
 */
public class HeapOomService {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();
        int i = 0;
        boolean flag = true;
        while(flag) {
            try {
                i++;
                list.add(new byte[1024 * 1024]); // 每次增加1M大小的数组对象
            }catch(Throwable e) {
                e.printStackTrace();
                flag = false;
                System.out.println("Count = " + i); // 记录运行的次数
            }
        }
    }
}
