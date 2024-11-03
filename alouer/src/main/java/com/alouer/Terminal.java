package com.alouer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alouer.collections.ClientCollection;
import com.alouer.collections.InstructorCollection;
import com.alouer.commands.Command;
import com.alouer.enums.UserType;
import com.alouer.factories.CommandFactory;
import com.alouer.factories.RegistrationFactory;
import com.alouer.models.Administrator;
import com.alouer.models.Client;
import com.alouer.models.Instructor;
import com.alouer.utils.ConsoleUtils;
import com.alouer.utils.DatabaseManager;

public class Terminal {
    private static boolean loggedIn = false;
    private static boolean applicationRunning = true;
    private static Object user;
    private static boolean debugMode = false;

    public static void main(String[] args) {
        run(true);
    }

    public static void run(boolean isDebugMode) {
        Scanner scanner = new Scanner(System.in);

        DatabaseManager.initializeDatabase(scanner);

        debugMode = isDebugMode;

        System.out.println("Welcome to the System. Please log in or register.");

        while (applicationRunning) {
            loggedIn = false;
            if (!debugMode) {
                ConsoleUtils.clearConsole();
            }

            System.out.println("Choose an option:");
            System.out.println("1. Log in");
            System.out.println("2. Register");
            System.out.println("3. EXIT");

            System.out.println("Select a number: ");
            String input = scanner.nextLine();

            int option;
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (option == 1) {
                while (!loggedIn) {
                    logIn(scanner);
                }
            } else if (option == 2) {
                runCommandLoop(RegistrationFactory.getRegistrationCommands(scanner), scanner);
            } else if (option == 3) {
                System.out.print("Exiting, have a nice day!");
                System.exit(0);
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }

        scanner.close();
    }

    private static void logIn(Scanner scanner) {
        System.out.println("Enter user type:");
        UserType[] userTypes = UserType.values();
        for (int i = 0; i < userTypes.length; i++) {
            System.out.println((i + 1) + ". " + userTypes[i]);
        }

        System.out.print("Select a number or -1 to exit login: ");
        String userTypeInput = scanner.nextLine();

        int userTypeIndex;
        try {
            userTypeIndex = Integer.parseInt(userTypeInput) - 1;
            if (userTypeIndex == -2) {
                loggedIn = true;
                return;
            }
            if (userTypeIndex < 0 || userTypeIndex >= userTypes.length) {
                System.out.println("Invalid selection. Please try again.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        UserType userType = userTypes[userTypeIndex];

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
                user = admin;
                loggedIn = true;
            } else {
                System.out.println("Invalid admin credentials. Please try again.");
            }
        }

        if (loggedIn) {
            Map<String, Command> userCommands = CommandFactory.getCommands(userType, user, scanner);
            runCommandLoop(userCommands, scanner);
            System.out.println("Successfully logged out, have a nice day!");
        }
    }

    private static void runCommandLoop(Map<String, Command> commandsMap, Scanner scanner) {
        List<String> commandNames = new ArrayList<>(commandsMap.keySet());
        List<Command> commands = new ArrayList<>(commandsMap.values());

        while (true) {
            if (!debugMode) {
                ConsoleUtils.clearConsole();
            }

            System.out.println("Available commands:");
            for (int i = 0; i < commandNames.size(); i++) {
                System.out.println((i + 1) + ". " + commandNames.get(i));
            }

            System.out.print("Enter a command number or -1 to return: ");
            String input = scanner.nextLine();

            try {
                int commandIndex = Integer.parseInt(input) - 1;
                if (commandIndex >= 0 && commandIndex < commands.size()) {
                    Command command = commands.get(commandIndex);
                    if (!debugMode) {
                        ConsoleUtils.clearConsole();
                    }
                    command.execute();

                    if ("Log Out".equalsIgnoreCase(commandNames.get(commandIndex))) {
                        break;
                    }
                } else if (commandIndex == -2) {
                    break;
                } else {
                    System.out.println("Invalid command number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
            System.out.println();
        }
    }
}
