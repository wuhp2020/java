package com.web.结构模式.外观模式;

public class Test {

    /**
     * 定义： 隐藏了系统的复杂性，并向客户端提供了一个可以访问系统的接口
     * 1）.门面角色：外观模式的核心。它被客户角色调用，它熟悉子系统的功能。内部根据客户角色的需求预定了几种功能的组合。（客户调用，同时自身调用子系统功能）
     * 2）.子系统角色:实现了子系统的功能。它对客户角色和Facade时未知的。它内部可以有系统内的相互交互，也可以由供外界调用的接口。（实现具体功能）
     * 3）.客户角色:通过调用Facede来完成要实现的功能（调用门面角色）
     * @param args
     */
    public static void main(String[] args) {
        Computer computer = new Computer();
        computer.start();
        System.out.println("=================");
        computer.shutDown();
    }
}
