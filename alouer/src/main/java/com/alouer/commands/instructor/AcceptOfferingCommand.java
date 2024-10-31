package com.alouer.commands.instructor;

import com.alouer.commands.Command;
import com.alouer.models.Instructor;
import com.alouer.models.Location;
import com.alouer.lessonManagement.Lesson;
import com.alouer.collections.LocationCollection;
import com.alouer.collections.LessonCollection;
import com.alouer.utils.ConsoleUtils;

import java.util.Arrays;
import java.util.List;

import java.util.Scanner;

public class AcceptOfferingCommand implements Command {
    private Instructor instructor;

    public AcceptOfferingCommand(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public void execute() {
        List<Location> locations = LocationCollection.getLocations();
        List<String> excludedProperties = Arrays.asList("Lessons");
        ConsoleUtils.printTable(locations, excludedProperties);
        System.out.println();

        System.out.print("Select a location by entering its ID: ");
        Scanner scanner = new Scanner(System.in);
        int locationId = scanner.nextInt();

        List<Lesson> lessons = LessonCollection.filterByLocation(locationId);
        List<String> excludedProperties2 = Arrays.asList("LocationId", "Booking");
        ConsoleUtils.printTable(lessons, excludedProperties2);
    }
}
