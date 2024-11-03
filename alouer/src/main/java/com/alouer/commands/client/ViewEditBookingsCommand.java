package com.alouer.commands.client;

import com.alouer.commands.Command;
import com.alouer.collections.BookingCollection;
import com.alouer.collections.ChildCollection;
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

public class ViewEditBookingsCommand implements Command {
    private Client client;
    private Scanner scanner;

    public ViewEditBookingsCommand(Client client, Scanner scanner) {
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        boolean keepViewing = true;

        List<Booking> clientBookings = BookingCollection.getByClientId(client.getId());

        if (clientBookings.isEmpty()) {
            System.out.println("You have no bookings.");
            return;
        }

        while (keepViewing) {
            clientBookings = BookingCollection.getByClientId(client.getId());

            if (clientBookings.isEmpty()) {
                System.out.println("You have no bookings.");
                return;
            }

            ConsoleUtils.printTable(clientBookings,
                    Arrays.asList("Id", "Client Id", "Lesson Id", "Child Id"));

            System.out.print("Enter the ID of a lesson to view details or type -1 to exit: ");
            int bookingId = requestBookingId(clientBookings);
            if (bookingId == -1) {
                keepViewing = false;
            }

            Booking selectedBooking = BookingCollection.getById(bookingId);

            if (selectedBooking == null) {
                System.out.println("The selected booking does not exist. Please try again");
            }

            Lesson selectedLesson = LessonCollection.getById(selectedBooking.getLessonId());

            Location location = LocationCollection.getById(selectedLesson.getLocationId());
            Instructor instructor = InstructorCollection.getById(selectedLesson.getAssignedInstructorId());
            Child child = ChildCollection.getById(selectedBooking.getChildId());

            displayLessonDetails(selectedLesson, location, instructor, child);

            System.out.print("Type 'd' to delete this booking or 'b' to go back: ");
            String userAction = scanner.next().trim().toLowerCase();

            if (userAction.equals("d") && BookingCollection.delete(bookingId)) {
                keepViewing = true;
                System.out.println("Successfully deleted the booking.");
            } else if (!userAction.equals("b")) {
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private void displayLessonDetails(Lesson lesson, Location location, Instructor instructor, Child child) {
        System.out.println("\nLesson Details:");
        System.out.println("Lesson ID: " + lesson.getId());
        System.out.println("Location: " + (location != null ? location.getName() : "Unknown"));
        System.out.println("Instructor: "
                + (instructor != null ? instructor.getFirstName() + " " + instructor.getLastName() : "Unknown"));
        if (child != null)
            System.out.println("Child: " + child.getFirstName() + " " + child.getLastName());
        System.out.println();
    }

    private Integer requestBookingId(List<Booking> clientBookings) {
        int bookingId = -1;
        boolean keepViewing = false;

        try {
            while (!keepViewing) {
                System.out.print("Enter a booking ID (or -1 to cancel): ");
                bookingId = scanner.nextInt();

                int maxBookingId = clientBookings.stream()
                        .mapToInt(Booking::getId)
                        .max()
                        .orElse(-1);

                if ((bookingId >= 0 && bookingId <= maxBookingId && BookingCollection.getById(bookingId) != null)
                        || bookingId == -1) {
                    keepViewing = true;
                } else {
                    System.out.println("Invalid booking ID. Please try again.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next();
        }
        return bookingId;
    }

}
