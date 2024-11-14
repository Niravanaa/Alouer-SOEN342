package com.alouer.commands.account;

import com.alouer.commands.Command;
import com.alouer.collections.InstructorCollection;

import java.util.Scanner;

public class RegisterInstructorCommand implements Command {
    private Scanner scanner;

    public RegisterInstructorCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {

        String firstName = requestFirstName();

        String lastName = requestLastName();

        String email = requestEmail();

        String password = requestPassword();

        String phoneNumber = requestPhoneNumber();

        if (InstructorCollection.getByEmail(email) != null) {
            System.out.println("\nAn instructor with this email already exists.");
            return;
        }

        if (InstructorCollection.createInstructor(firstName, lastName, email, password, phoneNumber)) {
            System.out.println("\nInstructor registered successfully.");
            return;
        } else {
            System.out.println("\nFailed to register instructor.");
            return;
        }
    }

    private String requestFirstName() {
        System.out.print("\nEnter instructor's first name: ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("[A-Za-z]+")) {
                return input;
            }
            System.out.println("\nInvalid name. Please enter a valid first name without numbers.");
        }
    }

    private String requestLastName() {
        System.out.print("\nEnter instructor's last name: ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("[A-Za-z]+")) {
                return input;
            }
            System.out.println("\nInvalid name. Please enter a valid last name without numbers.");
        }
    }

    private String requestEmail() {
        System.out.print("\nEnter instructor's email: ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return input;
            }
            System.out.println("\nInvalid email format. Please enter a valid email.");
        }
    }

    private String requestPassword() {
        System.out.print("\nEnter instructor's password:");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.length() >= 6) {
                return input;
            }
            System.out.println("\nPassword must be at least 6 characters long.");
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
