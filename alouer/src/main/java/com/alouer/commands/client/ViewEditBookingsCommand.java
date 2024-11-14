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

import java.util.ArrayList;
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

        List<Booking> clientBookings = BookingCollection.getByClientId(client.getId());

        if (clientBookings.isEmpty()) {
            System.out.println("\nYou have no bookings.");
            scanner.nextLine();
            return;
        }

        while (true) {
            clientBookings = BookingCollection.getByClientId(client.getId());

            if (clientBookings.isEmpty()) {
                System.out.println("\nYou have no bookings.");
                scanner.nextLine();
                return;
            }

            List<String> headers = new ArrayList<>();
            headers.add("Booking ID");
            headers.add("Lesson Title");
            headers.add("Location Name");
            headers.add("Instructor Name");
            headers.add("Child Name");

            List<Integer> bookingIds = new ArrayList<>();
            List<String> lessonTitles = new ArrayList<>();
            List<String> locationNames = new ArrayList<>();
            List<String> instructorNames = new ArrayList<>();
            List<String> childNames = new ArrayList<>();

            for (Booking booking : clientBookings) {
                Lesson lesson = LessonCollection.getById(booking.getLessonId());
                Location location = LocationCollection.getById(lesson.getLocationId());
                Instructor instructor = InstructorCollection.getById(lesson.getAssignedInstructorId());
                Child child = null;

                if (booking.getChildId() != null) {
                    child = ChildCollection.getById(booking.getChildId());
                }

                String locationName = location.getName();
                String instructorName = instructor.getFirstName();
                String childName = (child != null) ? child.getFirstName() + " " + child.getLastName() : "N/A";

                bookingIds.add(booking.getId());
                lessonTitles.add(lesson.getTitle());
                locationNames.add(locationName);
                instructorNames.add(instructorName);
                childNames.add(childName);
            }

            ConsoleUtils.printMultipleListsAsTable(
                    headers,
                    bookingIds,
                    lessonTitles,
                    locationNames,
                    instructorNames,
                    childNames);

            System.out.print("\nEnter the ID of a lesson to view details or type -1 to exit: ");
            int bookingId = requestBookingId(clientBookings);
            if (bookingId == -1) {
                break;
            }

            Booking selectedBooking = BookingCollection.getById(bookingId);

            if (selectedBooking == null) {
                System.out.println("\nThe selected booking does not exist. Please try again");
            }

            Lesson selectedLesson = LessonCollection.getById(selectedBooking.getLessonId());

            Location location = LocationCollection.getById(selectedLesson.getLocationId());
            Instructor instructor = InstructorCollection.getById(selectedLesson.getAssignedInstructorId());
            Child child = null;

            if (selectedBooking.getChildId() != null) {
                child = ChildCollection.getById(selectedBooking.getChildId());
            }

            displayBookingDetails(selectedLesson, location, instructor, child);

            System.out.print("\nType 'd' to delete this booking or 'b' to go back: ");
            String userAction = scanner.nextLine().trim().toLowerCase();

            if (userAction.equals("d") && BookingCollection.delete(bookingId)) {
                System.out.println("\nSuccessfully deleted the booking.");
            } else if (!userAction.equals("b")) {
                System.out.println("\nInvalid selection. Please try again.");
            }
        }
    }

    private void displayBookingDetails(Lesson lesson, Location location, Instructor instructor, Child child) {
        System.out.println("\nLesson Details:");
        System.out.println("Lesson ID: " + lesson.getId());
        System.out.println("Location: " + (location != null ? location.getName() : "Unknown"));
        System.out.println("Instructor: "
                + (instructor != null ? instructor.getFirstName() + " " + instructor.getLastName() : "Unknown"));
        if (child != null)
            System.out.println("Child: " + child.getFirstName() + " " + child.getLastName());
    }

    private Integer requestBookingId(List<Booking> clientBookings) {
        int bookingId = -1;

        try {
            while (true) {
                bookingId = Integer.parseInt(scanner.nextLine());

                if (bookingId == -1 || (BookingCollection.getById(bookingId) != null)) {
                    break;
                } else {
                    System.out.print("\nInvalid booking ID. Please try again: ");
                }
            }
        } catch (InputMismatchException e) {
            System.out.print("\nInvalid input. Please enter a valid integer: ");
        }
        return bookingId;
    }

}
