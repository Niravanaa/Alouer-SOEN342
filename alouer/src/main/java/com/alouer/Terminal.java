package com.alouer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alouer.collections.ClientCollection;
import com.alouer.collections.InstructorCollection;
import com.alouer.commands.Command;
import com.alouer.enums.*;
import com.alouer.factories.CommandFactory;
import com.alouer.models.Administrator;
import com.alouer.models.Client;
import com.alouer.models.Instructor;
import com.alouer.utils.ConsoleUtils;
import com.alouer.utils.DatabaseManager;

public class Terminal {
    private static boolean loggedIn = false;
    private static Object user;
    private static boolean debugMode = false;

    public static void main(String[] args) {
        run(true);
    }

    public static void run(boolean isDebugMode) {
        DatabaseManager.initializeDatabase();

        debugMode = isDebugMode;

        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        System.out.println("Welcome to the System. Please log in.");

        while (!loggedIn) {
            if (!debugMode) {
                ConsoleUtils.clearConsole();
            }

            System.out.println("Enter user type, or 4 to EXIT:");
            UserType[] userTypes = UserType.values();
            for (int i = 0; i < userTypes.length; i++) {
                System.out.println((i + 1) + ". " + userTypes[i]);
            }
            System.out.println("4. EXIT");

            System.out.print("Select a number: ");
            String userTypeInput = scanner.nextLine();

            int userTypeIndex;

            try {
                userTypeIndex = Integer.parseInt(userTypeInput) - 1;
                if (userTypeIndex == 3) { // Exit option
                    System.out.print("Exiting, have a nice day!");
                    scanner.close();
                    System.exit(0);
                } else if (userTypeIndex < 0 || userTypeIndex >= userTypes.length + 1) {
                    System.out.println("Invalid selection. Please try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
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
                commandLoop(userType, scanner);
                System.out.println("Successfully logged out, have a nice day!");
            }
        }

        scanner.close();
    }

    private static void commandLoop(UserType userType, Scanner scanner) {
        while (true) {
            if (!debugMode) {
                ConsoleUtils.clearConsole();
            }

            Map<String, Command> commandsMap = CommandFactory.getCommands(userType, user);
            List<Command> commands = new ArrayList<>(commandsMap.values());
            List<String> commandNames = new ArrayList<>(commandsMap.keySet());

            System.out.println("Available commands:");
            for (int i = 0; i < commandNames.size(); i++) {
                System.out.println((i + 1) + ". " + commandNames.get(i));
            }

            System.out.print("Enter a command number: ");
            String input = scanner.nextLine();

            try {
                int commandIndex = Integer.parseInt(input) - 1;

                if (commandIndex >= 0 && commandIndex < commands.size()) {
                    Command command = commands.get(commandIndex);
                    if (!debugMode) {
                        ConsoleUtils.clearConsole();
                    }
                    command.execute();

                    if ("logOut".equalsIgnoreCase(commandNames.get(commandIndex))) {
                        break;
                    }
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
