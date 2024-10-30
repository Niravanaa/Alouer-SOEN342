package admin;

import alouer.models.Administrator;

public class LogoutCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Logging out...");
    }
}