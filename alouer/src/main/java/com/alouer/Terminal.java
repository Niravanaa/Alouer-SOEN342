package com.alouer;

import java.util.Map;
import java.util.Scanner;

import com.alouer.collections.ClientCollection;
import com.alouer.collections.InstructorCollection;
import com.alouer.commands.Command;
import com.alouer.enums.UserType;
import com.alouer.factories.CommandFactory;
import com.alouer.models.Administrator;
import com.alouer.models.Client;
import com.alouer.models.Instructor;

public class Terminal {
    private static boolean loggedIn = false;
    private static Object user;
    private static boolean debugMode = false;

    public static void main(String[] args) {
        run(false);
    }

    public static void run(boolean isDebugMode) {
        debugMode = isDebugMode;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the System. Please log in.");

        while (!loggedIn) {
            if (!debugMode) {
                clearConsole();
            }

            System.out.print("Enter user type (CLIENT, INSTRUCTOR, ADMINISTRATOR), or EXIT to quit the program: ");
            String userTypeInput = scanner.nextLine().toUpperCase();
            UserType userType;
            try {
                userType = UserType.valueOf(userTypeInput);
            } catch (IllegalArgumentException e) {
                if (userTypeInput.toUpperCase().equals("EXIT")) {
                    System.out.println("Exiting, have a nice day!");
                    scanner.close();
                    System.exit(0);
                }
                System.out.println("Invalid user type. Please try again.");
                continue;
            }

            // Prompt for credentials
            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (userType == UserType.CLIENT) {
                Client client = ClientCollection.validateCredentials(email, password);
                if (client != null) {
                    user = client;
                    loggedIn = true;
                } else {
                    System.out.println("Invalid client credentials. Please try again.");
                }
            } else if (userType == UserType.INSTRUCTOR) {
                Instructor instructor = InstructorCollection.validateCredentials(email, password);
                if (instructor != null) {
                    user = instructor;
                    loggedIn = true;
                } else {
                    System.out.println("Invalid instructor credentials. Please try again.");
                }
            } else if (userType == UserType.ADMINISTRATOR) {
                Administrator admin = Administrator.getInstance();
                if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
                    admin.setConnected(true);
                    user = admin;
                    loggedIn = true;
                } else {
                    System.out.println("Invalid admin credentials. Please try again.");
                }
            }

            if (loggedIn) {
                commandLoop(userType, scanner);
                System.out.println("Successfully logged out, have a nice day!");
            }
        }

        scanner.close();
    }

    private static void commandLoop(UserType userType, Scanner scanner) {
        while (true) {
            if (!debugMode) {
                clearConsole();
            }

            Map<String, Command> commands = CommandFactory.getCommands(userType, user);

            System.out.println("Available commands:");
            for (String commandName : commands.keySet()) {
                System.out.println("- " + commandName);
            }

            System.out.print("Enter a command: ");
            String commandInput = scanner.nextLine();
            Command command = commands.get(commandInput);

            if (command != null) {
                command.execute();

                if ("logOut".equalsIgnoreCase(commandInput)) {
                    break;
                }
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
