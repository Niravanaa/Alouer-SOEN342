package com.alouer.commands.registration;

import com.alouer.commands.Command;
import com.alouer.collections.ClientCollection;

import java.util.Scanner;

public class RegisterClientCommand implements Command {
    private Scanner scanner;

    public RegisterClientCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {

        String firstName = requestFirstName();

        String lastName = requestLastName();

        String email = requestEmail();

        String password = requestPassword();

        if (ClientCollection.getByEmail(email) != null) {
            System.out.println("A client with this email already exists.");
            scanner.next();
            return;
        }

        boolean result = ClientCollection.createClient(firstName, lastName, email, password);
        if (result) {
            System.out.println("Client registered successfully.");
            return;
        } else {
            System.out.println("Failed to register client.");
            return;
        }
    }

    private String requestFirstName() {
        System.out.println("Enter client's first name:");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("[A-Za-z]+")) {
                return input;
            }
            System.out.println("Invalid name. Please enter a valid first name without numbers.");
        }
    }

    private String requestLastName() {
        System.out.println("Enter client's last name:");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("[A-Za-z]+")) {
                return input;
            }
            System.out.println("Invalid name. Please enter a valid last name without numbers.");
        }
    }

    private String requestEmail() {
        System.out.println("Enter client's email:");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return input;
            }
            System.out.println("Invalid email format. Please enter a valid email.");
        }
    }

    private String requestPassword() {
        System.out.println("Enter client's password:");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.length() >= 6) {
                return input;
            }
            System.out.println("Password must be at least 6 characters long.");
        }
    }
}
