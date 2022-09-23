package com.web.关系模式.责任链模式;

public class PresidentApprover extends Approver {
    public PresidentApprover(String Name) {
        super(Name + " PresidentApprover");
        // TODO Auto-generated constructor stub
    }
    @Override
    public void ProcessRequest(PurchaseRequest request) {
        // TODO Auto-generated method stub
        System.out.println("**This request " + request.GetID() + " will be handled by " + this.Name + " **");
    }
}
