package com.web.创建模式.工厂模式.简单工厂模式;

public class Test {
    public static void main(String[] args) {
        SimplePizzaFactory factory = new SimplePizzaFactory();
        factory.createPizza("cheese");
    }
}
