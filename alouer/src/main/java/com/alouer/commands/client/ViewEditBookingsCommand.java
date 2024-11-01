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
import com.alouer.lessonManagement.Booking;
import com.alouer.lessonManagement.Lesson;
import com.alouer.models.Location;
import com.alouer.utils.ConsoleUtils;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ViewEditBookingsCommand implements Command {
    private Client client;

    public ViewEditBookingsCommand(Client client) {
        this.client = client;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        boolean keepViewing = true;

        List<Booking> clientBookings = BookingCollection.getByClientId(client.getId());

        if (clientBookings.isEmpty()) {
            System.out.println("You have no bookings.");
            scanner.close();
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
            int bookingId = getLessonSelection(scanner);

            if (bookingId <= -1 || bookingId > clientBookings.size()) {
                keepViewing = false;
                continue;
            }

            Booking selectedBooking = null;

            for (Booking booking : clientBookings) {
                if (booking.getId() == bookingId) {
                    selectedBooking = booking;
                }
            }

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
                client.removeBooking(bookingId);
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

    private int getLessonSelection(Scanner scanner) {
        int lessonId = -1;
        try {
            lessonId = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next();
        }
        return lessonId;
    }
}
