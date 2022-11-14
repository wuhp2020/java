package com.web.结构模式.代理模式.静态代理;

public class Test {

    /**
     * 优点：可以做到在符合开闭原则的情况下对目标对象进行功能扩展。
     * 缺点： 代理对象与目标对象要实现相同的接口，我们得为每一个服务都得创建代理类，工作量太大，不易管理。
     * 同时接口一旦发生改变，代理类也得相应修改。
     * @param args
     */
    public static void main(String[] args) {
        BuyHouseProxy proxy = new BuyHouseProxy(new BuyHouseImpl());
        proxy.buyHosue();
    }
}
