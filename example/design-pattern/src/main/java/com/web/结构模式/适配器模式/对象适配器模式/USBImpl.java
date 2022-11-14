package com.web.结构模式.适配器模式.对象适配器模式;

public class USBImpl implements USB {
    @Override
    public void showPPT() {
        System.out.println("PPT内容演示");
    }
}
