package com.alouer.commands.instructor;

import com.alouer.commands.Command;
import com.alouer.models.Instructor;
import com.alouer.models.Location;
import com.alouer.lessonManagement.Lesson;
import com.alouer.collections.LocationCollection;
import com.alouer.collections.LessonCollection;
import com.alouer.utils.TablePrinter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.Scanner;

public class AcceptOfferingCommand implements Command {
    private Instructor instructor;

    public AcceptOfferingCommand(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public void execute() {
        List<Location> locations = LocationCollection.getLocations();
        Set<String> excludedProperties = new HashSet<>(Arrays.asList("Lessons"));
        TablePrinter.printTable(locations, excludedProperties);

        System.out.println("Select a location by entering its ID");
        Scanner scanner = new Scanner(System.in);
        int locationId = scanner.nextInt();

        List<Lesson> lessons = LessonCollection.filterByLocation(locationId);
        Set<String> excludedProperties2 = new HashSet<>(Arrays.asList("LocationId", "Booking"));
        TablePrinter.printTable(lessons, excludedProperties2);
    }
}