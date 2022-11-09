package com.web.关系模式.策略模式;

public class Environment {
    private Strategy strategy;
    public Environment(Strategy strategy) {
        this.strategy = strategy;
    }
    public int calculate(int a, int b) {
        return strategy.calc(a, b);
    }
}
