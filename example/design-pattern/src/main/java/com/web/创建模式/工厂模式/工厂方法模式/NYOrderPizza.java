package com.web.创建模式.工厂模式.工厂方法模式;

public class NYOrderPizza extends OrderPizza {

    @Override
    public Pizza createPizza(String ordertype) {
        Pizza pizza = null;
        if ("cheese".equals(ordertype)) {
            pizza = new NYCheesePizza();
        } else if ("pepper".equals(ordertype)) {
            pizza = new NYPepperPizza();
        }
        return pizza;
    }
}
