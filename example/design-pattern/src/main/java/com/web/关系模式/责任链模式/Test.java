package com.web.关系模式.责任链模式;

public class Test {
    /**
     * 定义：如果有多个对象有机会处理请求，责任链可使请求的发送者和接受者解耦，请求沿着责任链传递，直到有一个对象处理了它为止。
     * 主要解决：职责链上的处理者负责处理请求，客户只需要将请求发送到职责链上即可，无须关心请求的处理细节和请求的传递，所以职责链将请求的发送者和请求的处理者解耦了。
     * 何时使用：在处理消息的时候以过滤很多道。
     * 如何解决：拦截的类都实现统一接口。
     * 关键代码：Handler 里面聚合它自己，在 HandlerRequest 里判断是否合适，如果没达到条件则向下传递，向谁传递之前 set 进去。
     * @param args
     */
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
