package com.web.结构模式.装饰者模式;

public class Test {

    /**
     * 定义：动态的将新功能附加到对象上。在对象功能扩展方面，它比继承更有弹性
     * 举例(咖啡馆订单项目：1）、咖啡种类：Espresso、ShortBlack、LongBlack、Decaf2）、调料（装饰者）：Milk、Soy、Chocolate)
     * @param args
     */
    public static void main(String[] args) {
        Drink order;
        order = new Decaf();
        System.out.println("order1 price:" + order.cost());
        System.out.println("order1 desc:" + order.getDescription());
        System.out.println("****************");
        order = new LongBlack();
        order = new Milk(order);
        order = new Chocolate(order);
        order = new Chocolate(order);
        System.out.println("order2 price:" + order.cost());
        System.out.println("order2 desc:" + order.getDescription());
    }
}
