package com.alouer;

import java.util.Map;
import java.util.Scanner;
import enums.UserType;
import models.Client;
import commands.Command;

public class Terminal {
    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to the System. Please log in.");
        
        // Prompt for user type
        System.out.print("Enter user type (CLIENT, INSTRUCTOR, ADMIN): ");
        String userTypeInput = scanner.nextLine().toUpperCase();
        
        UserType userType;
        try {
            userType = UserType.valueOf(userTypeInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid user type.");
            return;
        }

        // Prompt for credentials
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Object user = null; // This will hold the authenticated user

        // Validate credentials based on user type
        if (userType == UserType.ADMIN) {
            Administrator admin = Administrator.getInstance();
            if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
                admin.setConnected(true);
                user = admin;
            } else {
                System.out.println("Invalid admin credentials.");
                return;
            }
        } else if (userType == UserType.CLIENT) {
            Client client = ClientCollection.validateCredentials(email, password);
            if (client != null) {
                user = client;
            } else {
                System.out.println("Invalid client credentials.");
                return;
            }
        } else if (userType == UserType.INSTRUCTOR) {
            Instructor instructor = InstructorCollection.validateCredentials(email, password);
            if (instructor != null) {
                user = instructor;
            } else {
                System.out.println("Invalid instructor credentials.");
                return;
            }
        }

        // Get available commands
        Map<String, Command> commands = CommandFactory.getCommands(userType, user);
        
        // Display available commands
        System.out.println("Available commands:");
        for (String commandName : commands.keySet()) {
            System.out.println("- " + commandName);
        }

        // Handle command input (example of command execution)
        System.out.print("Enter a command: ");
        String commandInput = scanner.nextLine();
        Command command = commands.get(commandInput);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid command.");
        }

        scanner.close();
    }
}
