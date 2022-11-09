package com.web.关系模式.命令模式;

public class Test {

    /**
     * 定义：将一个请求封装为一个对象，使发出请求的责任和执行请求的责任分割开。这样两者之间通过命令对象进行沟通，这样方便将命令对象进行储存、传递、调用、增加与管理。
     * 意图：将一个请求封装成一个对象，从而使您可以用不同的请求对客户进行参数化。
     * 主要解决：在软件系统中，行为请求者与行为实现者通常是一种紧耦合的关系，但某些场合，比如需要对行为进行记录、撤销或重做、事务等处理时，这种无法抵御变化的紧耦合的设计就不太合适。
     * 何时使用：在某些场合，比如要对行为进行"记录、撤销/重做、事务"等处理，这种无法抵御变化的紧耦合是不合适的。在这种情况下，如何将"行为请求者"与"行为实现者"解耦？将一组行为抽象为对象，可以实现二者之间的松耦合。
     * 如何解决：通过调用者调用接受者执行命令，顺序：调用者→接受者→命令
     * @param args
     */
    public static void main(String[] args) {
        Contral contral = new Contral();
        TurnOffLight turnOffLight = new TurnOffLight(new Light("AAA"));
        contral.CommandExcute(turnOffLight);
        contral.CommandUndo(turnOffLight);
    }
}
