package com.alouer.commands.instructor;

import com.alouer.commands.Command;
import com.alouer.models.Instructor;
import com.alouer.models.Location;
import com.alouer.models.lessonManagement.Lesson;
import com.alouer.collections.LocationCollection;
import com.alouer.collections.LessonCollection;
import com.alouer.utils.ConsoleUtils;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AcceptOfferingCommand implements Command {
    private Instructor instructor;
    private Scanner scanner;

    public AcceptOfferingCommand(Instructor instructor, Scanner scanner) {
        this.instructor = instructor;
        this.scanner = scanner;
    }

    @Override
    public void execute() {

        List<Location> locations = LocationCollection.getLocations();
        ConsoleUtils.printTable(locations, Arrays.asList("Lessons"));

        System.out.print("\nSelect a location by entering its ID: ");
        int locationId = requestLocationId(locations);

        List<Lesson> availableLessons = LessonCollection.getUnassignedLessons(locationId);
        ConsoleUtils.printTable(availableLessons,
                Arrays.asList("Location Id", "Is Available", "Booking", "Assigned Instructor Id", "Id", "Schedule"));

        if (availableLessons.isEmpty()) {
            System.out.println("\nNo available lessons found for the selected location.");
            return;
        }

        Lesson selectedLesson = requestLessonId(availableLessons);
        if (selectedLesson == null) {
            System.out.println("\nInvalid lesson selection. Assignment process aborted.");
            return;
        }

        if (selectedLesson.getAssignedInstructorId() == null) {
            selectedLesson.setAssignedInstructorId(instructor.getId());
            System.out.println("\nLesson successfully assigned to you!");
        } else {
            System.out.println("\nLesson is already assigned to another instructor.");
        }
    }

    private Integer requestLocationId(List<Location> locations) {
        int locationId = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                locationId = Integer.parseInt(scanner.nextLine());
                if (LocationCollection.getById(locationId) != null) {
                    validInput = true;
                } else {
                    System.out.print("\nLocation ID does not exist. Please enter a location ID: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("\nInvalid input. Please enter a location ID: ");
                scanner.nextLine();
            }
        }
        return locationId;
    }

    private Lesson requestLessonId(List<Lesson> lessons) {
        Lesson selectedLesson = null;
        boolean invalidSelection = true;

        while (invalidSelection) {
            System.out.print("\nSelect a lesson by ID: ");
            try {
                int selection = Integer.parseInt(scanner.nextLine());

                if (LessonCollection.getById(selection) != null) {
                    selectedLesson = LessonCollection.getById(selection);
                    invalidSelection = false;
                } else {
                    System.out.print("\nInvalid lesson ID. Please enter an integer: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("\nInvalid input. Please enter an integer: ");
            }
        }
        return selectedLesson;
    }
}
