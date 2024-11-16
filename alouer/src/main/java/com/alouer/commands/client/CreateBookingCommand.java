package com.alouer.commands.client;

import com.alouer.commands.Command;
import com.alouer.models.Client;
import com.alouer.models.Child;
import com.alouer.models.Location;
import com.alouer.models.lessonManagement.Lesson;
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
    private Scanner scanner;

    public CreateBookingCommand(Client client, Scanner scanner) {
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        List<Location> locations = LocationCollection.getLocations();
        ConsoleUtils.printTable(locations, Arrays.asList("Lessons"));

        System.out.print("\nEnter the location ID you wish to view available lessons for: ");
        int locationId = getValidLocationId(locations);

        List<Lesson> availableLessons = LessonCollection.getAvailableLessons(locationId);
        ConsoleUtils.printTable(availableLessons,
                Arrays.asList("Location Id", "Is Available", "Booking", "Assigned Instructor Id", "Id"));

        if (availableLessons.isEmpty()) {
            System.out.println("\nNo available lessons found for the selected location.");
            return;
        }

        Integer lessonId = selectLesson(availableLessons);
        if (lessonId == null) {
            System.out.println("\nInvalid selection. Booking process aborted.");
            return;
        }

        List<Child> children = ChildCollection.getChildrenByClientId(client.getId());
        Integer childId = null;

        if (children != null) {
            System.out.print("\nIs this booking for an underage dependent? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                if (children.isEmpty()) {
                    System.out.println("\nNo children found for this client.");
                    return;
                }

                ConsoleUtils.printTable(children, Arrays.asList("Id", "Parent Id"));
                System.out.print("\nEnter the child ID you wish to book for, or enter -1 to skip child selection: ");
                childId = getValidChildId(children);

                if (childId == null) {
                    System.out.println("\nInvalid child ID. Booking process aborted.");
                    return;
                }
            }
        }

        if (BookingCollection.validateBooking(lessonId, client.getId())) {
            BookingCollection.createBooking(client.getId(), lessonId, childId);
            System.out.println("\nBooking created successfully!");
        } else {
            System.out.println(
                    "\nBooking validation failed, this lesson intersects with an existing booking that you made.");
        }
    }

    private Integer selectLesson(List<Lesson> lessons) {
        boolean invalidSelection = true;

        Integer selection = null;

        while (invalidSelection) {
            System.out.print("\nSelect a lesson by ID: ");
            try {
                selection = Integer.parseInt(scanner.nextLine());

                if (LessonCollection.getById(selection) != null) {
                    invalidSelection = false;
                } else {
                    System.out.println("\nNo lesson found with that ID.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
        return selection;
    }

    private int getValidLocationId(List<Location> locations) {
        int locationId = -1;
        boolean invalidInput = true;

        while (invalidInput) {
            System.out.print("Please enter a valid location ID (integer): ");
            try {
                locationId = Integer.parseInt(scanner.nextLine());

                if (LocationCollection.getById(locationId) != null) {
                    invalidInput = false;
                } else {
                    System.out.println("Location ID does not exist. Please enter a valid location ID.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
        return locationId;
    }

    private Integer getValidChildId(List<Child> children) {
        Integer childId = null;
        boolean validInput = false;

        while (!validInput) {
            try {
                int enteredId = Integer.parseInt(scanner.nextLine());

                if (enteredId == -1) {
                    return -1;
                }

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
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
        return childId;
    }
}
