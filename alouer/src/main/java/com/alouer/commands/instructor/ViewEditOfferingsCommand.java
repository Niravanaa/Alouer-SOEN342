package com.alouer.commands.instructor;

import com.alouer.commands.Command;
import com.alouer.collections.BookingCollection;
import com.alouer.collections.ChildCollection;
import com.alouer.collections.ClientCollection;
import com.alouer.collections.InstructorCollection;
import com.alouer.collections.LessonCollection;
import com.alouer.collections.LocationCollection;
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

public class ViewEditOfferingsCommand implements Command {
    private Instructor instructor;
    private Scanner scanner;

    public ViewEditOfferingsCommand(Instructor instructor, Scanner scanner) {
        this.instructor = instructor;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        List<Lesson> instructorLessons = LessonCollection.getByInstructorId(instructor.getId());

        if (instructorLessons.isEmpty()) {
            System.out.println("\nYou have no assigned lessons.");
            scanner.nextLine();
            return;
        }

        while (true) {
            instructorLessons = LessonCollection.getByInstructorId(instructor.getId());

            if (instructorLessons.isEmpty()) {
                System.out.println("\nYou have no assigned lessons.");
                return;
            }

            ConsoleUtils.printTable(instructorLessons,
                    Arrays.asList("Id", "Location Id", "Is Available", "Assigned Instructor Id", "Booking Id",
                            "Booking"));

            System.out.print("\nEnter the ID of a lesson to view details or type -1 to exit: ");
            Lesson selectedLesson = requestLessonId(instructorLessons);

            if (selectedLesson == null) {
                break;
            }

            Location location = LocationCollection.getById(selectedLesson.getLocationId());
            List<Booking> bookings = BookingCollection.getByLessonId(selectedLesson.getId());

            displayLessonDetails(selectedLesson, location, bookings);

            System.out.print("\nType 'u' to un-assign yourself from this offering or 'b' to go back: ");
            String userAction = scanner.nextLine().trim().toLowerCase();

            if (userAction.equals("u")) {
                selectedLesson.setAssignedInstructorId(null);
                LessonCollection.updateLesson(selectedLesson);
                InstructorCollection.update(instructor);
                System.out.println("\nSuccessfully unassigned from the lesson.");
            } else if (!userAction.equals("b")) {
                System.out.println("\nInvalid selection. Please try again.");
                scanner.nextLine();
            }
        }
    }

    private void displayLessonDetails(Lesson lesson, Location location, List<Booking> bookings) {
        System.out.println("\nLesson Details:");
        System.out.println("Lesson Title: " + lesson.getTitle());
        System.out.println("Location: " + (location != null ? location.getName() : "Unknown"));

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
        boolean invalidSelection = true;

        while (invalidSelection) {
            try {
                int selection = Integer.parseInt(scanner.nextLine());

                if (selection == -1) {
                    return null;
                }

                for (Lesson lesson : lessons) {
                    if (lesson.getId() == selection) {
                        selectedLesson = lesson;
                        invalidSelection = false;
                        break;
                    }
                }

                if (invalidSelection) {
                    System.out.print("Invalid lesson ID. Please enter a valid lesson ID: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
        return selectedLesson;
    }
}
