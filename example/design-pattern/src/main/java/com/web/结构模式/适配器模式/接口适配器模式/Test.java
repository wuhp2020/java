package com.web.结构模式.适配器模式.接口适配器模式;

public class Test {

    /**
     * 当不需要全部实现接口提供的方法时，可先设计一个抽象类实现接口，并为该接口中每个方法提供一个默认实现（空方法），
     * 那么该抽象类的子类可有选择地覆盖父类的某些方法来实现需求，它适用于一个接口不想使用其所有的方法的情况
     * @param args
     */
    public static void main(String[] args) {
        //通过适配器创建一个VGA对象，这个适配器实际是使用的是USB的showPPT（）方法
        VGA a = new AdapterUSB2VGAImpl();
        //进行投影
        Projector p1 = new Projector();
        p1.projection(a);
    }
}
