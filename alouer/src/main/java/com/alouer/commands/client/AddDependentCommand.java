package com.alouer.commands.client;

import com.alouer.commands.Command;
import com.alouer.models.Client;
import com.alouer.collections.ChildCollection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class AddDependentCommand implements Command {
    private Client client;
    private Scanner scanner;

    public AddDependentCommand(Client client, Scanner scanner) {
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        String firstName = requestFirstName();
        if (firstName == null)
            return;

        String lastName = requestLastName();
        if (lastName == null)
            return;

        LocalDate dateOfBirth = requestDateOfBirth();
        if (dateOfBirth == null)
            return;

        if (ChildCollection.validateChild(client.getId(), firstName, lastName, dateOfBirth)) {
            System.out.println("\nA dependent with these details already exists.");
            return;
        }

        Integer newChildId = ChildCollection.createChild(client.getId(), firstName, lastName, dateOfBirth);
        if (newChildId != null) {
            System.out.println("\nDependent added successfully.");
        } else {
            System.out.println("\nFailed to add dependent.");
        }
    }

    private String requestFirstName() {
        System.out.print("\nEnter dependent's first name: ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("[A-Za-z]+")) {
                return input;
            }
            System.out.println("Invalid name. Please enter a valid first name without numbers.");
        }
    }

    private String requestLastName() {
        System.out.print("\nEnter dependent's last name: ");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.matches("[A-Za-z]+")) {
                return input;
            }
            System.out.println("Invalid name. Please enter a valid last name without numbers.");
        }
    }

    private LocalDate requestDateOfBirth() {
        System.out.print("\nEnter dependent's date of birth (e.g., 'Jan 01, 2010' or '01-01-2010'): ");

        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("MMM dd, yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        };

        while (true) {
            String input = scanner.nextLine().trim();
            LocalDate dateOfBirth = null;

            for (DateTimeFormatter formatter : formatters) {
                try {
                    dateOfBirth = LocalDate.parse(input, formatter);
                    break;
                } catch (DateTimeParseException e) {
                }
            }

            if (dateOfBirth != null) {
                LocalDate today = LocalDate.now();
                if (dateOfBirth.isAfter(today)) {
                    System.out.println("Date of birth cannot be in the future.");
                } else if (today.minusYears(18).isBefore(dateOfBirth)) {
                    return dateOfBirth;
                } else {
                    System.out.println("Dependent must be under 18 years old.");
                }
            } else {
                System.out.println("Invalid date format. Please use a format like 'Jan 01, 2010' or '01-01-2010'.");
            }
        }
    }
}
