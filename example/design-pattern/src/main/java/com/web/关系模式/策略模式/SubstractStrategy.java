package com.web.关系模式.策略模式;

public class SubstractStrategy implements Strategy {
    @Override
    public int calc(int num1, int num2) {
        // TODO Auto-generated method stub
        return num1 - num2;
    }
}
