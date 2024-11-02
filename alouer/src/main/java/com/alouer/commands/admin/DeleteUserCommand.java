package com.alouer.commands.admin;

import com.alouer.models.Administrator;
import com.alouer.models.Client;
import com.alouer.models.Instructor;
import com.alouer.utils.ConsoleUtils;
import com.alouer.enums.UserType;
import com.alouer.collections.ClientCollection;
import com.alouer.collections.InstructorCollection;
import com.alouer.commands.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DeleteUserCommand implements Command {
    private Administrator admin;

    public DeleteUserCommand(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        UserType userType = requestUserType(scanner);
        if (userType == null)
            return;

        if (userType == UserType.CLIENT) {
            List<Client> clients = ClientCollection.getClients();
            if (clients.isEmpty()) {
                System.out.println("No clients available to delete.");
                return;
            }

            ConsoleUtils.printTable(clients,
                    Arrays.asList("Id", "Password", "Role", "Children", "Booking"));
            int userId = requestUserId(scanner, clients);
            if (userId == -1)
                return;

            if (confirmDeletion(scanner)) {
                ClientCollection.delete(userId);
                System.out.println("Client with ID " + userId + " has been deleted.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else if (userType == UserType.INSTRUCTOR) {
            List<Instructor> instructors = InstructorCollection.getInstructors();
            if (instructors.isEmpty()) {
                System.out.println("No instructors available to delete.");
                return;
            }
            ConsoleUtils.printTable(instructors, Arrays.asList("Password", "Role", "Lessons"));
            int userId = requestUserId(scanner, instructors);
            if (userId == -1)
                return;

            if (confirmDeletion(scanner)) {
                InstructorCollection.delete(userId);
                System.out.println("Instructor with ID " + userId + " has been deleted.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        }
    }

    private UserType requestUserType(Scanner scanner) {
        System.out.println("Would you like to delete a CLIENT or INSTRUCTOR?");
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                UserType userType = UserType.valueOf(input);
                if (userType == UserType.CLIENT || userType == UserType.INSTRUCTOR) {
                    return userType;
                } else {
                    System.out.println("Only CLIENT or INSTRUCTOR can be deleted. Try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please enter CLIENT or INSTRUCTOR.");
            }
        }
    }

    private Integer requestUserId(Scanner scanner, List<?> users) {
        System.out.println("Please enter the ID of the user you wish to delete: ");
        while (true) {
            try {
                int userId = Integer.parseInt(scanner.nextLine());
                for (Object user : users) {
                    if ((user instanceof Client && ((Client) user).getId() == userId) ||
                            (user instanceof Instructor && ((Instructor) user).getId() == userId)) {
                        return userId;
                    }
                }
                System.out.println("Invalid ID. Please enter a valid user ID from the list.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric ID.");
            }
        }
    }

    private boolean confirmDeletion(Scanner scanner) {
        System.out.println("Are you sure you want to delete this user? This action is irreversible. (yes/no)");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        return confirmation.equals("yes");
    }
}
