package com.web.关系模式.责任链模式;

public class Test {
    public static void main(String[] args) {
        Client mClient = new Client();
        Approver GroupLeader = new GroupApprover("Tom");
        Approver DepartmentLeader = new DepartmentApprover("Jerry");
        Approver VicePresident = new VicePresidentApprover("Kate");
        Approver President = new PresidentApprover("Bush");
        GroupLeader.SetSuccessor(VicePresident);
        DepartmentLeader.SetSuccessor(President);
        VicePresident.SetSuccessor(DepartmentLeader);
        President.SetSuccessor(GroupLeader);
        GroupLeader.ProcessRequest(mClient.sendRequst(1, 10000, 40));
    }
}
