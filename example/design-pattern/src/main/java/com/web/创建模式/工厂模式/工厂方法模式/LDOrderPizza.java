package com.web.创建模式.工厂模式.工厂方法模式;


public class LDOrderPizza extends OrderPizza {

    @Override
    public Pizza createPizza(String ordertype) {
        Pizza pizza = null;
        if ("cheese".equals(ordertype)) {
            pizza = new LDCheesePizza();
        } else if ("pepper".equals(ordertype)) {
            pizza = new LDPepperPizza();
        }
        return pizza;
    }
}
