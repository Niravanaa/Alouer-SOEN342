package admin;

import alouer.models.Administrator;

public class DeleteUserCommand implements Command {
    private Administrator admin;

    public DeleteUserCommand(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        // Logic to delete a user
        System.out.println("Deleting a user...");
    }
}