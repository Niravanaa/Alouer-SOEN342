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

        System.out.print("Select a location by entering its ID: ");
        int locationId = requestLocationId(locations);

        List<Lesson> availableLessons = LessonCollection.getUnassignedLessons(locationId);
        ConsoleUtils.printTable(availableLessons,
                Arrays.asList("Location Id", "Is Available", "Booking", "Assigned Instructor Id", "Id"));

        if (availableLessons.isEmpty()) {
            System.out.println("No available lessons found for the selected location.");
            return;
        }

        Lesson selectedLesson = requestLessonId(availableLessons);
        if (selectedLesson == null) {
            System.out.println("Invalid lesson selection. Assignment process aborted.");
            return;
        }

        if (selectedLesson.getAssignedInstructorId() == null) {
            selectedLesson.setAssignedInstructorId(instructor.getId());
            selectedLesson.setAvailable(true);
            instructor.addLesson(selectedLesson.getId());
            System.out.println("Lesson successfully assigned to you!");
        } else {
            System.out.println("Lesson is already assigned to another instructor.");
        }
    }

    private Integer requestLocationId(List<Location> locations) {
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

    private Lesson requestLessonId(List<Lesson> lessons) {
        Lesson selectedLesson = null;
        boolean validSelection = false;

        while (!validSelection) {
            System.out.print("Select a lesson by ID: ");
            try {
                int selection = scanner.nextInt();

                for (Lesson lesson : lessons) {
                    if (lesson.getId() == selection) {
                        selectedLesson = lesson;
                        validSelection = true;
                        break;
                    }
                }

                if (!validSelection) {
                    System.out.println("Invalid lesson ID. Please enter a valid lesson ID.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }
        return selectedLesson;
    }
}
