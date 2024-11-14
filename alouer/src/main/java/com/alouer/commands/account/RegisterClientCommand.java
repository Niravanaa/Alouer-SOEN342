package com.alouer.commands.account;

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

        String phoneNumber = requestPhoneNumber();

        if (ClientCollection.getByEmail(email) != null) {
            System.out.println("\nA client with this email already exists.");
            return;
        }

        if (ClientCollection.createClient(firstName, lastName, email, password, phoneNumber)) {
            System.out.println("\nClient registered successfully.");
            return;
        } else {
            System.out.println("\nFailed to register client.");
            return;
        }
    }

    private String requestFirstName() {
        System.out.print("\nEnter client's first name: ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("[A-Za-z]+")) {
                return input;
            }
            System.out.println("Invalid name. Please enter a valid first name without numbers.");
        }
    }

    private String requestLastName() {
        System.out.print("\nEnter client's last name: ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("[A-Za-z]+")) {
                return input;
            }
            System.out.println("Invalid name. Please enter a valid last name without numbers.");
        }
    }

    private String requestEmail() {
        System.out.print("\nEnter client's email: ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return input;
            }
            System.out.println("Invalid email format. Please enter a valid email.");
        }
    }

    private String requestPassword() {
        System.out.print("\nEnter client's password: ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.length() >= 6) {
                return input;
            }
            System.out.println("Password must be at least 6 characters long.");
        }
    }

    private String requestPhoneNumber() {
        System.out.print("\nEnter client's phone number (format: xxx-xxx-xxxx): ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("\\d{3}-\\d{3}-\\d{4}")) {
                return input;
            }
            System.out.println("Phone number must be in the format xxx-xxx-xxxx.");
        }
    }

}
