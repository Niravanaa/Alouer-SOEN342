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
import com.alouer.utils.Session;

public class Terminal {
    private static boolean loggedIn = false;
    private static Object user;

    public static void main(String[] args) {
        run(true);
    }

    public static void run(boolean isDebugMode) {
        Scanner scanner = new Scanner(System.in);

        DatabaseManager.initializeDatabase(scanner);

        while (true) {
            ConsoleUtils.printSystemLogo();

            System.out.println("Welcome to the Alouer system.");

            loggedIn = false;

            System.out.println("\nChoose an option:");
            System.out.println("1. Log in");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            int option;

            try {
                System.out.print("\nSelect a number: ");
                String input = scanner.nextLine();
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.");
                continue;
            }

            if (option == 1) {
                while (!loggedIn) {
                    logIn(scanner);
                }
            } else if (option == 2) {
                runCommandLoop(RegistrationFactory.getRegistrationCommands(scanner), scanner);
            } else if (option == 3) {
                System.out.println("\nExiting, have a nice day!");
                break;
            } else {
                System.out.println("\nInvalid selection. Please try again.");
            }
        }

        scanner.close();
    }

    private static void logIn(Scanner scanner) {
        System.out.println("\nEnter user type:");
        UserType[] userTypes = UserType.values();
        for (int i = 0; i < userTypes.length; i++) {
            System.out.println((i + 1) + ". " + userTypes[i]);
        }

        System.out.print("\nSelect a number or -1 to exit login: ");
        String userTypeInput = scanner.nextLine();

        int userTypeIndex;
        try {
            userTypeIndex = Integer.parseInt(userTypeInput) - 1;
            if (userTypeIndex == -2) {
                loggedIn = true;
                return;
            }
            if (userTypeIndex < 0 || userTypeIndex >= userTypes.length) {
                System.out.println("\nInvalid selection. Please try again.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a number.");
            return;
        }

        UserType userType = userTypes[userTypeIndex];

        System.out.print("\nEnter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userType == UserType.CLIENT) {
            Client client = ClientCollection.validateCredentials(email, password);
            if (client != null) {
                if (Session.hasActiveSession(client.getId(), UserType.CLIENT)) {
                    System.out
                            .println("\nThis account is already logged in on another instance. Please log out first.");
                    return;
                }

                user = client;
                Session session = new Session(client.getId(), UserType.CLIENT);
                if (session.save()) {
                    loggedIn = true;
                    System.out.println("\nClient logged in successfully. Session created.");
                } else {
                    System.out.println("\nFailed to create session for client.");
                }
            } else {
                System.out.println("\nInvalid client credentials. Please try again.");
            }
        } else if (userType == UserType.INSTRUCTOR) {
            Instructor instructor = InstructorCollection.validateCredentials(email, password);
            if (instructor != null) {
                if (Session.hasActiveSession(instructor.getId(), UserType.INSTRUCTOR)) {
                    System.out
                            .println("\nThis account is already logged in on another instance. Please log out first.");
                    return;
                }

                user = instructor;
                Session session = new Session(instructor.getId(), UserType.INSTRUCTOR);
                if (session.save()) {
                    loggedIn = true;
                    System.out.println("\nInstructor logged in successfully. Session created.");
                } else {
                    System.out.println("\nFailed to create session for instructor.");
                }
            } else {
                System.out.println("\nInvalid instructor credentials. Please try again.");
            }
        } else if (userType == UserType.ADMINISTRATOR) {
            Administrator admin = Administrator.getInstance();
            if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
                if (Session.hasActiveSession(admin.getEmail())) {
                    System.out
                            .println("\nThis account is already logged in on another instance. Please log out first.");
                    return;
                }

                user = admin;
                Session session = new Session(admin.getEmail(), UserType.ADMINISTRATOR);
                if (session.save()) {
                    loggedIn = true;
                    System.out.println("\nAdministrator logged in successfully. Session created.");
                } else {
                    System.out.println("\nFailed to create session for admin.");
                }
            } else {
                System.out.println("\nInvalid admin credentials. Please try again.");
            }
        }

        if (loggedIn) {
            Map<String, Command> userCommands = CommandFactory.getCommands(user, scanner);
            runCommandLoop(userCommands, scanner);
        }
    }

    private static void runCommandLoop(Map<String, Command> commandsMap, Scanner scanner) {
        List<String> commandNames = new ArrayList<>(commandsMap.keySet());
        List<Command> commands = new ArrayList<>(commandsMap.values());

        while (true) {
            System.out.println("\nAvailable commands:");
            for (int i = 0; i < commandNames.size(); i++) {
                System.out.println((i + 1) + ". " + commandNames.get(i));
            }

            System.out.print("\nEnter a command number, or enter -1 to return: ");
            String input = scanner.nextLine();

            try {
                int commandIndex = Integer.parseInt(input) - 1;
                if (commandIndex >= 0 && commandIndex < commands.size()) {
                    Command command = commands.get(commandIndex);

                    command.execute();

                    if ("Log Out".equalsIgnoreCase(commandNames.get(commandIndex))
                            || "Exit".equalsIgnoreCase(commandNames.get(commandIndex))) {
                        break;
                    }
                } else if (commandIndex < 0 && commandIndex == -2) {
                    break;
                } else {
                    System.out.println("Invalid command number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.print("\nPlease enter a valid number.");
            }
        }
    }
}
