package com.alouer.commands.instructor;

import com.alouer.commands.Command;
import com.alouer.collections.BookingCollection;
import com.alouer.collections.ClientCollection;
import com.alouer.collections.LessonCollection;
import com.alouer.collections.LocationCollection;
import com.alouer.models.Client;
import com.alouer.models.Instructor;
import com.alouer.models.Location;
import com.alouer.lessonManagement.Booking;
import com.alouer.lessonManagement.Lesson;
import com.alouer.utils.ConsoleUtils;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ViewEditOfferingsCommand implements Command {
    private Instructor instructor;

    public ViewEditOfferingsCommand(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        boolean keepViewing = true;

        List<Lesson> instructorLessons = LessonCollection.getByInstructorId(instructor.getId());

        if (instructorLessons.isEmpty()) {
            System.out.println("You have no assigned lessons.");
            scanner.close();
            return;
        }

        while (keepViewing) {
            instructorLessons = LessonCollection.getByInstructorId(instructor.getId());

            if (instructorLessons.isEmpty()) {
                System.out.println("You have no assigned lessons.");
                return;
            }

            ConsoleUtils.printTable(instructorLessons,
                    Arrays.asList("Id", "Location Id", "Is Available", "Assigned Instructor Id", "Booking Id"));

            System.out.print("Enter the ID of a lesson to view details or type -1 to exit: ");
            int lessonId = getLessonSelection(scanner);

            if (lessonId <= -1 || lessonId > instructorLessons.size()) {
                keepViewing = false;
                continue;
            }

            Lesson selectedLesson = null;

            for (Lesson lesson : instructorLessons) {
                if (lesson.getId() == lessonId) {
                    selectedLesson = lesson;
                }
            }

            if (selectedLesson == null) {
                System.out.println("The selected lesson does not exist. Please try again.");
                continue;
            }

            Location location = LocationCollection.getById(selectedLesson.getLocationId());
            Booking booking = BookingCollection.getByLessonId(selectedLesson.getId());
            Client client = booking != null ? ClientCollection.getById(booking.getClientId()) : null;

            displayLessonDetails(selectedLesson, location, client);

            System.out.print("Type 'u' to un-assign yourself from this offering or 'b' to go back: ");
            String userAction = scanner.next().trim().toLowerCase();

            if (userAction.equals("u")) {
                selectedLesson.setAssignedInstructorId(null);
                instructor.removeLesson(lessonId);
                keepViewing = true;
            } else if (!userAction.equals("b")) {
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private void displayLessonDetails(Lesson lesson, Location location, Client client) {
        System.out.println("\nLesson Details:");
        System.out.println("Lesson ID: " + lesson.getId());
        System.out.println("Location: " + (location != null ? location.getName() : "Unknown"));
        System.out.println("Client: "
                + (client != null ? client.getFirstName() + " " + client.getLastName() : "No client assigned"));
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
