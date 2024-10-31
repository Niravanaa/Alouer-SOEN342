package com.alouer.commands;

import com.alouer.models.Administrator;
import com.alouer.models.Client;
import com.alouer.models.Instructor;

public class LogOutCommand implements Command {
    private Object user;

    public LogOutCommand(Object user) {
        this.user = user;
    }

    @Override
    public void execute() {
        if (user instanceof Client) {
            Client client = (Client) user;
            client.setConnected(false);
        } else if (user instanceof Instructor) {
            Instructor instructor = (Instructor) user;
            instructor.setConnected(false);
        } else if (user instanceof Administrator) {
            Administrator admin = (Administrator) user;
            admin.setConnected(false);
        }
    }
}
