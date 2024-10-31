package com.alouer.commands.client;

import com.alouer.commands.Command;
import com.alouer.models.Client;
import com.alouer.lessonManagement.Lesson;
import com.alouer.collections.BookingCollection;
import com.alouer.models.Location;
import com.alouer.utils.ConsoleUtils;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import com.alouer.collections.LessonCollection;
import com.alouer.collections.LocationCollection;

public class CreateBookingCommand implements Command {
    private Client client;

    public CreateBookingCommand(Client client) {
        this.client = client;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        List<Location> locations = LocationCollection.getLocations();

        ConsoleUtils.printTable(locations, Arrays.asList("Lessons"));

        System.out.print("Enter the location ID you wish to view available lessons for: ");
        int locationId = getValidLocationId(scanner, locations);

        List<Lesson> availableLessons = LessonCollection.getAvailableLessonsByLocationId(locationId);
        ConsoleUtils.printTable(availableLessons,
                Arrays.asList("Location Id", "Is Available", "Booking", "Assigned Instructor Id", "Id"));

        if (availableLessons.isEmpty()) {
            System.out.println("No available lessons found for the selected location.");
            return;
        }

        Lesson selectedLesson = selectLesson(scanner, availableLessons);

        if (selectedLesson == null) {
            System.out.println("Invalid selection. Booking process aborted.");
            return;
        }

        boolean isValid = BookingCollection.validateBooking(selectedLesson, client);

        if (isValid) {
            BookingCollection.createBooking(client.getId(), selectedLesson.getId());
            System.out.println("Booking created successfully!");
        } else {
            System.out.println("Booking validation failed.");
        }
    }

    private Lesson selectLesson(Scanner scanner, List<Lesson> lessons) {
        Lesson selectedLesson = null;
        boolean validSelection = false;

        while (!validSelection) {
            System.out.print("Select a lesson by ID: ");
            try {
                int selection = scanner.nextInt();

                if (selection < 0 || selection >= lessons.size()) {
                    System.out.println("Invalid selection.");
                } else {
                    selectedLesson = lessons.get(selection);
                    validSelection = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }

        return selectedLesson;
    }

    private static int getValidLocationId(Scanner scanner, List<Location> locations) {
        int locationId = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Please enter a valid location ID (integer): ");
            try {
                locationId = scanner.nextInt();

                if (locationId < 0 || locationId >= locations.size()) {
                    System.out.println("Location ID does not exist. Please enter a valid location ID.");
                } else {
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }
        return locationId;
    }

}
