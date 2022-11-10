package com.web.创建模式.工厂模式.抽象工厂模式;

public class LDFactory implements AbsFactory {
    @Override
    public Pizza CreatePizza(String ordertype) {
        Pizza pizza = null;
        if ("cheese".equals(ordertype)) {
            pizza = new LDCheesePizza();
        } else if ("pepper".equals(ordertype)) {
            pizza = new LDPepperPizza();
        }
        return pizza;
    }
}
