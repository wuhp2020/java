package com.web.关系模式.责任链模式;

public class VicePresidentApprover extends Approver {
    public VicePresidentApprover(String Name) {
        super(Name + " VicePresidentApprover");
    }
    @Override
    public void ProcessRequest(PurchaseRequest request) {
        // TODO Auto-generated method stub
        System.out.println("**This request " + request.GetID()
                    + " will be handled by " + this.Name + " **");
    }
}
