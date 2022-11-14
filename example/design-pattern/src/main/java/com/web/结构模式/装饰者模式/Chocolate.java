package com.web.结构模式.装饰者模式;

public class Chocolate extends Decorator {
    public Chocolate(Drink Obj) {
        super(Obj);
        // TODO Auto-generated constructor stub
        super.setDescription("Chocolate");
        super.setPrice(2.0f);
    }
}
