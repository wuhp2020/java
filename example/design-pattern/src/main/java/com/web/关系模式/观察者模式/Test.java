package com.web.关系模式.观察者模式;

public class Test {
    public static void main(String[] args) {
        // 被观察者
        WechatServer server = new WechatServer();

        // 注册观察者
        Observer userZhang = new User("ZhangSan");
        Observer userLi = new User("LiSi");
        Observer userWang = new User("WangWu");
        server.registerObserver(userZhang);
        server.registerObserver(userLi);
        server.registerObserver(userWang);

        server.setInfomation("PHP是世界上最好用的语言！");
        System.out.println("----------------------------------------------");
        server.removeObserver(userZhang);
        server.setInfomation("JAVA是世界上最好用的语言！");
    }
}
