package com.web.结构模式.装饰者模式;

public class Milk extends Decorator {
    public Milk(Drink Obj) {
        super(Obj);
        // TODO Auto-generated constructor stub
        super.setDescription("Milk");
        super.setPrice(2.0f);
    }
}
