package com.web.结构模式.装饰者模式;

import lombok.Data;

@Data
public abstract class Drink {

    public String description = "";
    private float price = 0f;

    public abstract float cost();
}
