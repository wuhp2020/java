package com.web.创建模式.建造者模式;

public class Test {

    public static void main(String[] args) {
        Director director = new Director();
        director.setComputerBuilder(new HPComputerBuilder());
        director.constructComputer();
    }
}
