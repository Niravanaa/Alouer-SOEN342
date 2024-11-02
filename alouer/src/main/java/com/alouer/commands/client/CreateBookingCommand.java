package com.alouer.commands.client;

import com.alouer.commands.Command;
import com.alouer.models.Client;
import com.alouer.models.Child;
import com.alouer.models.Location;
import com.alouer.lessonManagement.Lesson;
import com.alouer.collections.BookingCollection;
import com.alouer.collections.ChildCollection;
import com.alouer.collections.LessonCollection;
import com.alouer.collections.LocationCollection;
import com.alouer.utils.ConsoleUtils;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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

        List<Lesson> availableLessons = LessonCollection.getAvailableLessons(locationId);
        ConsoleUtils.printTable(availableLessons,
                Arrays.asList("Location Id", "Is Available", "Booking", "Assigned Instructor Id", "Id"));

        if (availableLessons.isEmpty()) {
            System.out.println("No available lessons found for the selected location.");
            return;
        }

        Integer lessonId = selectLesson(scanner, availableLessons);
        if (lessonId == null) {
            System.out.println("Invalid selection. Booking process aborted.");
            return;
        }

        List<Child> children = ChildCollection.getChildrenByClientId(client.getId());
        Integer childId = null;

        if (children != null) {
            System.out.print("Is this booking for an underage dependent? (yes/no): ");
            String response = scanner.next().trim().toLowerCase();

            if (response.equals("yes")) {
                if (children.isEmpty()) {
                    System.out.println("No children found for this client.");
                    return;
                }

                ConsoleUtils.printTable(children, Arrays.asList("Id", "Parent Id"));
                System.out.print("Enter the child ID you wish to book for: ");
                childId = getValidChildId(scanner, children);
                if (childId == null) {
                    System.out.println("Invalid child ID. Booking process aborted.");
                    return;
                }
            }
        }

        boolean isValid = BookingCollection.validateBooking(lessonId);
        if (isValid) {
            int newBookingId = BookingCollection.createBooking(client.getId(), lessonId, childId);
            client.addBooking(newBookingId);
            System.out.println("Booking created successfully!");
        } else {
            System.out.println("Booking validation failed.");
        }
    }

    private Integer selectLesson(Scanner scanner, List<Lesson> lessons) {
        boolean validSelection = false;

        int maxId = lessons.stream()
                .mapToInt(Lesson::getId)
                .max()
                .orElse(-1);

        Integer selection = null;

        while (!validSelection) {
            System.out.print("Select a lesson by ID: ");
            try {
                selection = scanner.nextInt();

                if (selection < 0 || selection > maxId) {
                    System.out.println("Invalid selection.");
                } else if (LessonCollection.getById(selection) != null) {
                    validSelection = true;
                } else {
                    System.out.println("No lesson found with that ID.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }
        return selection;
    }

    private static int getValidLocationId(Scanner scanner, List<Location> locations) {
        int locationId = -1;
        boolean validInput = false;

        int maxId = locations.stream()
                .mapToInt(Location::getId)
                .max()
                .orElse(-1);

        while (!validInput) {
            System.out.print("Please enter a valid location ID (integer): ");
            try {
                locationId = scanner.nextInt();

                if (locationId < 0 || locationId > maxId) {
                    System.out.println("Location ID does not exist. Please enter a valid location ID.");
                } else if (LocationCollection.getById(locationId) != null) {
                    validInput = true;
                } else {
                    System.out.println("Location ID does not exist. Please enter a valid location ID.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }
        return locationId;
    }

    private static Integer getValidChildId(Scanner scanner, List<Child> children) {
        Integer childId = null;
        boolean validInput = false;

        while (!validInput) {
            try {
                int enteredId = scanner.nextInt();
                for (Child child : children) {
                    if (child.getId() == enteredId) {
                        childId = enteredId;
                        validInput = true;
                        break;
                    }
                }
                if (!validInput) {
                    System.out.print("Invalid child ID. Please enter a valid child ID: ");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }
        return childId;
    }
}
