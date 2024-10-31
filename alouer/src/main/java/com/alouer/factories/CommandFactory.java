package com.alouer.factories;

import java.util.HashMap;
import java.util.Map;

import com.alouer.commands.*;
import com.alouer.commands.instructor.*;
import com.alouer.commands.admin.*;
import com.alouer.commands.client.*;
import com.alouer.models.*;
import com.alouer.enums.UserType;

public class CommandFactory {
    public static Map<String, Command> getCommands(UserType userType, Object user) {
        Map<String, Command> commands = new HashMap<>();

        commands.put("logOut", new LogOutCommand(user));

        if (userType == UserType.INSTRUCTOR) {
            commands.put("acceptOffering", new AcceptOfferingCommand((Instructor) user));
            commands.put("viewEditOfferings", new ViewEditOfferingsCommand((Instructor) user));
        } else if (userType == UserType.CLIENT) {
            commands.put("addDependent", new AddDependentCommand((Client) user));
            commands.put("createBooking", new CreateBookingCommand((Client) user));
            commands.put("viewEditBookings", new ViewEditBookingsCommand((Client) user));
        } else {
            commands.put("createLesson", new CreateLessonCommand((Administrator) user));
            commands.put("deleteUser", new DeleteUserCommand((Administrator) user));
        }

        return commands;
    }
}
