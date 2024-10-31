package com.alouer.commands.admin;

import com.alouer.models.Administrator;
import com.alouer.commands.Command;

public class CreateLessonCommand implements Command {
    private Administrator admin;

    public CreateLessonCommand(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        // Logic to create a lesson
        System.out.println("Creating a lesson...");
    }
}