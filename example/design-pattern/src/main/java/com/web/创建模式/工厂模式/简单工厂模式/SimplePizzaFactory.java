package com.web.创建模式.工厂模式.简单工厂模式;

public class SimplePizzaFactory {
    public Pizza createPizza(String ordertype) {
        Pizza pizza = null;
        if ("cheese".equals(ordertype)) {
            pizza = new CheesePizza();
        } else if ("greek".equals(ordertype)) {
            pizza = new GreekPizza();
        } else if ("pepper".equals(ordertype)) {
            pizza = new PepperPizza();
        }
        return pizza;
    }
}
