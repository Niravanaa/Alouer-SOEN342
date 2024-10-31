package com.alouer.commands.client;

import com.alouer.commands.Command;
import com.alouer.models.Client;

public class CreateBookingCommand implements Command {
    private Client client;

    public CreateBookingCommand(Client client) {
        this.client = client;
    }

    @Override
    public void execute() {
        // Logic to create a booking
        System.out.println("Creating a booking...");
    }
}