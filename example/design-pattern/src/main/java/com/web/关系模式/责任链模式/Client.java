package com.web.关系模式.责任链模式;

public class Client {
    public Client() {
    }
    public PurchaseRequest sendRequst(int Type, int Number, float Price) {
        return new PurchaseRequest(Type, Number, Price);
    }
}
