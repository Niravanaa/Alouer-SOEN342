package com.alouer.commands.account;

import com.alouer.commands.Command;
import com.alouer.enums.UserType;
import com.alouer.models.Administrator;
import com.alouer.models.Client;
import com.alouer.models.Instructor;
import com.alouer.utils.Session;

public class LogOutCommand implements Command {
    private Object user;

    public LogOutCommand(Object user) {
        this.user = user;
    }

    @Override
    public void execute() {
        if (user instanceof Client) {
            Client client = (Client) user;
            Session.deleteSession(client.getId(), UserType.CLIENT);
        } else if (user instanceof Instructor) {
            Instructor instructor = (Instructor) user;
            Session.deleteSession(instructor.getId(), UserType.INSTRUCTOR);
        } else if (user instanceof Administrator) {
            Administrator admin = (Administrator) user;
            Session.deleteSession(admin.getEmail());
        }

        System.out.println("\nLogged out successfully.");
    }
}
