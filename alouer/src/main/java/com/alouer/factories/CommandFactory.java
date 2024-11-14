package com.alouer.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.alouer.commands.*;
import com.alouer.commands.account.LogOutCommand;
import com.alouer.commands.instructor.*;
import com.alouer.commands.admin.*;
import com.alouer.commands.client.*;
import com.alouer.models.*;

public class CommandFactory {
    public static Map<String, Command> getCommands(Object user, Scanner scanner) {
        Map<String, Command> commands = new HashMap<>();

        commands.put("Log Out", new LogOutCommand(user));

        if (user instanceof Instructor) {
            commands.put("Accept an Offering", new AcceptOfferingCommand((Instructor) user, scanner));
            commands.put("View/Edit Assigned Offering(s)", new ViewEditOfferingsCommand((Instructor) user, scanner));
        } else if (user instanceof Client) {
            commands.put("Add a Dependent", new AddDependentCommand((Client) user, scanner));
            commands.put("Book a Lesson", new CreateBookingCommand((Client) user, scanner));
            commands.put("View/Edit Current Booking(s)", new ViewEditBookingsCommand((Client) user, scanner));
        } else {
            commands.put("Create a Lesson", new CreateLessonCommand(scanner));
            commands.put("Delete a User", new DeleteUserCommand(scanner));
            commands.put("View or Delete Lessons", new ViewDeleteLessonsCommand(scanner));
        }

        return commands;
    }
}
