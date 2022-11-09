package com.web.创建模式.工厂模式.工厂方法模式;

public class Test {
    /**
     * 定义: 定义了一个创建对象的抽象方法, 由子类决定要实例化的类, 工厂方法模式将对象的实例化推迟到子类.
     * 举例:（我们依然举pizza工厂的例子, 不过这个例子中, pizza产地有两个, 伦敦和纽约）.
     * 添加了一个新的产地, 如果用简单工厂模式的的话, 我们要去修改工厂代码, 并且会增加一堆的if else语句.
     * 而工厂方法模式克服了简单工厂要修改代码的缺点, 它会直接创建两个工厂, 纽约工厂和伦敦工厂.
     * @param args
     */
    public static void main(String[] args) {
        OrderPizza mOrderPizza = new NYOrderPizza();
    }
}
