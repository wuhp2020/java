package com.web.创建模式.工厂模式.抽象工厂模式;

public class Test {

    /**
     * 定义：定义了一个接口用于创建相关或有依赖关系的对象族，而无需明确指定具体类。
     * 举例：（我们依然举pizza工厂的例子，pizza工厂有两个：纽约工厂和伦敦工厂）
     * @param args
     */
    public static void main(String[] args) {
        AbsFactory factory = new LDFactory();
        Pizza pizza = factory.CreatePizza("cheese");
        System.out.println(pizza);
    }
}
