package com.alouer.commands.admin;

import com.alouer.commands.Command;
import com.alouer.collections.LessonCollection;
import com.alouer.collections.LocationCollection;
import com.alouer.collections.BookingCollection;
import com.alouer.collections.ChildCollection;
import com.alouer.collections.ClientCollection;
import com.alouer.collections.InstructorCollection;
import com.alouer.models.Child;
import com.alouer.models.Client;
import com.alouer.models.Instructor;
import com.alouer.models.Location;
import com.alouer.models.lessonManagement.Booking;
import com.alouer.models.lessonManagement.Lesson;
import com.alouer.utils.ConsoleUtils;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ViewDeleteLessonsCommand implements Command {
    private Scanner scanner;

    public ViewDeleteLessonsCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        while (true) {
            List<Location> locations = LocationCollection.getLocations();

            ConsoleUtils.printTable(locations, Arrays.asList("Lessons"));

            System.out.print("\nEnter a location ID to view its corresponding lessons, or -1 to return: ");
            int locationId = requestLocationId(locations);

            if (locationId == -1) {
                break;
            }

            List<Lesson> allLessons = LessonCollection.getLessonsByLocationId(locationId);

            if (allLessons.isEmpty()) {
                System.out.println("\nNo lessons available.");
                scanner.nextLine();
                return;
            }

            ConsoleUtils.printTable(allLessons,
                    Arrays.asList("Id", "Location Id", "Is Available", "Booking Id", "Booking",
                            "Assigned Instructor Id"));

            System.out.print("\nEnter the ID of a lesson to view details or type -1 to exit: ");
            Lesson selectedLesson = requestLessonId(allLessons);
            if (selectedLesson == null) {
                break;
            }

            Location location = LocationCollection.getById(selectedLesson.getLocationId());
            List<Booking> bookings = BookingCollection.getByLessonId(selectedLesson.getId());
            Instructor instructor = InstructorCollection.getById(selectedLesson.getAssignedInstructorId());

            displayLessonDetails(selectedLesson, location, bookings, instructor);

            System.out.print("\nType 'd' to delete this unassigned lesson or 'b' to go back: ");
            String userAction = scanner.nextLine().trim().toLowerCase();

            if (userAction.equals("d") && LessonCollection.delete(selectedLesson.getId())) {
                System.out.println("\nLesson deleted successfully.");
                scanner.nextLine();
            } else if (!userAction.equals("b")) {
                System.out.println(
                        "\nThe lesson cannot be deleted as it is assigned to an instructor.");
                scanner.nextLine();
            }
        }
    }

    private Integer requestLocationId(List<Location> locations) {
        int locationId = -1;
        while (true) {
            try {
                String input = scanner.nextLine();
                locationId = Integer.parseInt(input);

                if (LocationCollection.getById(locationId) != null || locationId == -1) {
                    return locationId;
                } else {
                    System.out.print("\nInvalid ID. Please enter a valid location ID: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("\nInvalid input. Please enter a numeric ID: ");
            }
        }
    }

    private void displayLessonDetails(Lesson lesson, Location location, List<Booking> bookings, Instructor instructor) {
        System.out.println("\nLesson Details:");
        System.out.println("Lesson Title: " + lesson.getTitle());
        System.out.println("Location: " + location.getName());
        System.out.println("Instructor: "
                + (lesson.getAssignedInstructorId() != null ? instructor.getFirstName() + " " + instructor.getLastName()
                        : "None"));

        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                Client client = ClientCollection.getById(booking.getClientId());

                if (client != null) {
                    System.out.print("Client: " + client.getFirstName() + " " + client.getLastName());

                    if (booking.getChildId() != null) {
                        Child child = ChildCollection.getById(booking.getChildId());
                        System.out.print(" (" + child.getFirstName() + " " + child.getLastName()
                                + " - dependent under " + client.getFirstName() + ")");
                    }
                    System.out.println();
                }
            }
        } else {
            System.out.println("Client(s): None");
        }
    }

    private Lesson requestLessonId(List<Lesson> lessons) {
        Lesson selectedLesson = null;
        boolean validSelection = false;

        while (!validSelection) {
            try {
                int selection = Integer.parseInt(scanner.nextLine());

                if (selection == -1) {
                    return selectedLesson;
                }

                for (Lesson lesson : lessons) {
                    if (lesson.getId() == selection) {
                        selectedLesson = lesson;
                        validSelection = true;
                        break;
                    }
                }

                if (!validSelection) {
                    System.out.print("Invalid lesson ID. Please enter a valid lesson ID: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
        return selectedLesson;
    }
}
