package com.alouer.commands.admin;

import com.alouer.models.Administrator;
import com.alouer.models.Location;
import com.alouer.utils.TablePrinter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alouer.collections.LocationCollection;
import com.alouer.commands.Command;

public class CreateLessonCommand implements Command {
    private Administrator admin;

    public CreateLessonCommand(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        List<Location> locations = LocationCollection.getLocations();

        Set<String> excludedProperties = new HashSet<>(Arrays.asList("Id", "Lessons"));

        TablePrinter.printTable(locations, excludedProperties);
    }
}