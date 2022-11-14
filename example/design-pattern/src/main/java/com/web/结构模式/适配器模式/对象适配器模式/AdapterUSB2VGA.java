package com.web.结构模式.适配器模式.对象适配器模式;

public class AdapterUSB2VGA implements VGA {
    USB u = new USBImpl();
    @Override
    public void projection() {
        u.showPPT();
    }
}
