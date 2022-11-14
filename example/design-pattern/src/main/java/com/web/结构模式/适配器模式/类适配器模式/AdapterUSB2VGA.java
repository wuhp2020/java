package com.web.结构模式.适配器模式.类适配器模式;

public class AdapterUSB2VGA extends USBImpl implements VGA{
    @Override
    public void projection() {
        super.showPPT();
    }
}
