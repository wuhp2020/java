package com.web.关系模式.命令模式;

public class Contral {
    public void CommandExcute(Command command) {
        // TODO Auto-generated method stub
        command.excute();
    }
    public void CommandUndo(Command command) {
        // TODO Auto-generated method stub
        command.undo();
    }
}
