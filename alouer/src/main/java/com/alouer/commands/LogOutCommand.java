package com.alouer.commands;

public class LogOutCommand implements Command {
    private Object user;

    public LogOutCommand(Object user) {
        this.user = user;
    }

    @Override
    public void execute() {
    }
}
