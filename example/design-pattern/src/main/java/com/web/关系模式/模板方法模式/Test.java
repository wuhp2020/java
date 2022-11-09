package com.web.关系模式.模板方法模式;

public class Test {
    /**
     * 定义: 定义一个操作中算法的骨架, 而将一些步骤延迟到子类中, 模板方法使得子类可以不改变算法的结构即可重定义该算法的某些特定步骤.
     * 抽象父类（AbstractClass）: 实现了模板方法, 定义了算法的骨架.
     * 具体类（ConcreteClass): 实现抽象类中的抽象方法, 即不同的对象的具体实现细节.
     * 举例: 我们做菜可以分为三个步骤 （1）备料 （2）具体做菜 （3）盛菜端给客人享用
     * 这三部就是算法的骨架, 然而做不同菜需要的料, 做的方法, 以及如何盛装给客人享用都是不同的这个就是不同的实现细节.
     * @param args
     */
    public static void main(String[] args) {
        Dish eggsWithTomato = new EggsWithTomato();
        eggsWithTomato.dodish();
        System.out.println("-----------------------------");
        Dish bouilli = new Bouilli();
        bouilli.dodish();
    }
}
