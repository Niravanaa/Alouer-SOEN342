package com.alouer.commands.admin;

import com.alouer.models.Administrator;
import com.alouer.models.Location;
import com.alouer.utils.ConsoleUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.alouer.collections.LessonCollection;
import com.alouer.collections.LocationCollection;
import com.alouer.commands.Command;
import com.alouer.enums.LessonType;

public class CreateLessonCommand implements Command {
    private Administrator admin;

    public CreateLessonCommand(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        List<Location> locations = LocationCollection.getLocations();

        ConsoleUtils.printTable(locations, Arrays.asList("Lessons"));

        System.out.println("\nEnter a location ID to view its corresponding lessons: ");

        int locationId = -1;
        while (true) {
            try {
                String input = scanner.nextLine();
                locationId = Integer.parseInt(input);

                if (locationId - 1 >= -1 && locationId - 1 < locations.size()) {
                    break;
                } else {
                    System.out.println("Invalid ID. Please enter a valid location ID: ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric ID: ");
            }
        }

        // Request lesson type
        LessonType lessonType = requestLessonTypeInput(scanner);

        // Request startTime and endTime
        String startTime = requestTimeInput(scanner, "start time (HH:mm)");
        String endTime = requestTimeInput(scanner, "end time (HH:mm)");

        // Validate that endTime is greater than startTime
        while (!isEndTimeGreaterThanStartTime(startTime, endTime)) {
            System.out.println("End time must be greater than start time. Please enter valid times again.");
            startTime = requestTimeInput(scanner, "start time (HH:mm)");
            endTime = requestTimeInput(scanner, "end time (HH:mm)");
        }

        // Request title of lesson
        String title = requestTitle(scanner);

        // Request schedule
        String schedule = requestScheduleInput(scanner);

        // Confirmation
        System.out.println("\nPlease confirm the details:");
        System.out.println("Location ID: " + locationId);
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Schedule: " + schedule);
        System.out.println("Is this correct? (yes/no)");

        String confirmation = scanner.nextLine().trim().toLowerCase();
        if ("yes".equals(confirmation) && LessonCollection.validateLesson(locationId, startTime, endTime, schedule)
                && LessonCollection.createLesson(locationId, title, lessonType, startTime, endTime, schedule)) {
            System.out.println("Successfully created a new lesson.");
        } else {
            System.out.println("There was an error creating the lesson");
        }
    }

    private LessonType requestLessonTypeInput(Scanner scanner) {
        LessonType lessonType = null;
        while (true) {
            System.out.println("Please enter the lesson type (PRIVATE or GROUP): ");
            String input = scanner.nextLine().trim().toUpperCase();

            try {
                lessonType = LessonType.valueOf(input);
                return lessonType;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid lesson type. Please enter either 'PRIVATE' or 'GROUP'.");
            }
        }
    }

    private String requestTimeInput(Scanner scanner, String timeType) {
        String time;
        while (true) {
            System.out.println("Please enter the " + timeType + ": ");
            time = scanner.nextLine().trim();
            if (time.matches("^(2[0-3]|[01]?[0-9]):[0-5][0-9]$")) {
                return time;
            } else {
                System.out.println("Invalid time format. Please use military time (HH:mm).");
            }
        }
    }

    // Helper method to check if endTime is greater than startTime
    private boolean isEndTimeGreaterThanStartTime(String startTime, String endTime) {
        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHours = Integer.parseInt(startParts[0]);
        int startMinutes = Integer.parseInt(startParts[1]);
        int endHours = Integer.parseInt(endParts[0]);
        int endMinutes = Integer.parseInt(endParts[1]);

        return (endHours > startHours) || (endHours == startHours && endMinutes > startMinutes);
    }

    // Helper method to request schedule input
    private String requestScheduleInput(Scanner scanner) {
        String schedule;
        String[] validDays = { "M", "Tu", "W", "Th", "F", "Sa", "Su" };
        while (true) {
            System.out.println("Please enter the schedule (e.g., M-Tu-W): ");
            schedule = scanner.nextLine().trim();
            String[] days = schedule.split("-");

            boolean valid = true;
            for (String day : days) {
                if (!Arrays.asList(validDays).contains(day)) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                return schedule;
            } else {
                System.out.println("Invalid schedule format. Please use the format M-Tu-W-Th-F-Sa-Su.");
            }
        }
    }

    private String requestTitle(Scanner scanner) {
        String title;
        while (true) {
            System.out.println("Please enter the lesson title: ");
            title = scanner.nextLine().trim();

            // Validate title is not empty and has a reasonable length
            if (!title.isEmpty() && title.length() <= 100) {
                return title;
            } else if (title.isEmpty()) {
                System.out.println("Title cannot be empty. Please enter a valid title.");
            } else {
                System.out.println("Title is too long. Please keep it under 100 characters.");
            }
        }
    }
}