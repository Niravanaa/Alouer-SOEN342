package com.alouer.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.alouer.commands.Command;
import com.alouer.commands.registration.RegisterInstructorCommand;
import com.alouer.commands.registration.RegisterClientCommand;

public class RegistrationFactory {
    public static Map<String, Command> getRegistrationCommands(Scanner scanner) {
        Map<String, Command> commands = new HashMap<>();
        commands.put("Register as Instructor", new RegisterInstructorCommand(scanner));
        commands.put("Register as Client", new RegisterClientCommand(scanner));
        return commands;
    }
}
