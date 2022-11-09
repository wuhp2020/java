package com.web.创建模式.工厂模式.简单工厂模式;

public class Test {
    /**
     * 定义: 定义了一个创建对象的类, 由这个类来封装实例化对象的行为
     * 举例: pizza工厂一共生产三种类型的pizza, chesse,pepper,greak. 通过工厂类（SimplePizzaFactory）实例化这三种类型的对象
     * @param args
     */
    public static void main(String[] args) {
        SimplePizzaFactory factory = new SimplePizzaFactory();
        factory.createPizza("cheese");
    }
}
