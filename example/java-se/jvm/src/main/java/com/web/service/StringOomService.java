package com.web.service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/9
 * @ Desc   : 描述
 */
public class StringOomService {
    static String base = "string";
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String str = base + base;
            base = str;
            // 存入常量
            list.add(str.intern());
        }
    }
}
