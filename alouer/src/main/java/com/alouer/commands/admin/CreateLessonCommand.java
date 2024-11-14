package com.alouer.commands.admin;

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
    private Scanner scanner;

    public CreateLessonCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        List<Location> locations = LocationCollection.getLocations();

        ConsoleUtils.printTable(locations, Arrays.asList("Lessons"));

        System.out.print("\nEnter the location ID for which you wish to create a new lesson: ");
        int locationId = requestLocationId(locations);

        LessonType lessonType = requestLessonTypeInput();

        String startTime = requestTimeInput("start time (HH:mm)");
        String endTime = requestTimeInput("end time (HH:mm)");

        while (!isEndTimeGreaterThanStartTime(startTime, endTime)) {
            System.out.println("\nEnd time must be greater than start time. Please enter valid times again.");
            startTime = requestTimeInput("start time (HH:mm)");
            endTime = requestTimeInput("end time (HH:mm)");
        }

        String title = requestTitle();

        String schedule = requestScheduleInput();

        String confirmation = requestConfirmation(locationId, startTime, endTime, schedule);

        if ("yes".equals(confirmation) && LessonCollection.validateLesson(locationId, startTime, endTime, schedule)
                && LessonCollection.createLesson(locationId, title, lessonType, startTime, endTime, schedule)) {
            System.out.println("\nSuccessfully created a new lesson.");
        } else if ("no".equals(confirmation)) {
            System.out.println("\nLesson creation terminated.");
        } else {
            System.out.println("\nThere was an error creating the lesson.");
        }
    }

    private String requestConfirmation(Integer locationId, String startTime, String endTime, String schedule) {
        String response;

        while (true) {
            System.out.println("\nPlease confirm the details:");
            System.out.println("Location ID: " + locationId);
            System.out.println("Start Time: " + startTime);
            System.out.println("End Time: " + endTime);
            System.out.println("Schedule: " + schedule);

            System.out.print("\nIs this correct? (yes/no): ");
            response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes") || response.equals("no")) {
                break;
            } else {
                System.out.println("\nInvalid input. Please enter 'yes' or 'no'.");
            }
        }

        return response;
    }

    private Integer requestLocationId(List<Location> locations) {
        int locationId = -1;
        while (true) {
            try {
                String input = scanner.nextLine();
                locationId = Integer.parseInt(input);

                boolean validId = false;
                for (Location location : locations) {
                    if (location.getId() == locationId) {
                        validId = true;
                        break;
                    }
                }

                if (validId) {
                    return locationId;
                } else {
                    System.out.print("\nInvalid ID. Please enter a valid location ID: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("\nInvalid input. Please enter a numeric ID: ");
            }
        }
    }

    private LessonType requestLessonTypeInput() {
        LessonType lessonType = null;
        while (true) {
            System.out.print("\nPlease enter the lesson type (PRIVATE or GROUP): ");
            String input = scanner.nextLine().trim().toUpperCase();

            try {
                lessonType = LessonType.valueOf(input);
                return lessonType;
            } catch (IllegalArgumentException e) {
                System.out.println("\nInvalid lesson type.");
            }
        }
    }

    private String requestTimeInput(String timeType) {
        String time;
        while (true) {
            System.out.print("\nPlease enter the " + timeType + ": ");
            time = scanner.nextLine().trim();
            if (time.matches("^(2[0-3]|[01]?[0-9]):[0-5][0-9]$")) {
                return time;
            } else {
                System.out.println("\nInvalid time format. Please use military time (HH:mm).");
            }
        }
    }

    private boolean isEndTimeGreaterThanStartTime(String startTime, String endTime) {
        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHours = Integer.parseInt(startParts[0]);
        int startMinutes = Integer.parseInt(startParts[1]);
        int endHours = Integer.parseInt(endParts[0]);
        int endMinutes = Integer.parseInt(endParts[1]);

        return (endHours > startHours) || (endHours == startHours && endMinutes > startMinutes);
    }

    private String requestScheduleInput() {
        String schedule;
        String[] validDays = { "M", "Tu", "W", "Th", "F", "Sa", "Su" };
        while (true) {
            System.out.print("\nPlease enter the schedule (e.g., M-Tu-W): ");
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
                System.out.println("\nInvalid schedule format. Please use the format M-Tu-W-Th-F-Sa-Su.");
            }
        }
    }

    private String requestTitle() {
        String title;
        while (true) {
            System.out.print("\nPlease enter the lesson title: ");
            title = scanner.nextLine().trim();

            if (!title.isEmpty() && title.length() <= 100) {
                return title;
            } else if (title.isEmpty()) {
                System.out.println("\nTitle cannot be empty. Please enter a valid title.");
            } else {
                System.out.println("\nTitle is too long. Please keep it under 100 characters.");
            }
        }
    }
}