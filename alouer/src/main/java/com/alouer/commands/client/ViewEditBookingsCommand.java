package com.alouer.commands.client;

import com.alouer.commands.Command;
import com.alouer.models.Client;

public class ViewEditBookingsCommand implements Command {
    private Client client;

    public ViewEditBookingsCommand(Client client) {
        this.client = client;
    }

    @Override
    public void execute() {
        // Logic to view/edit bookings
        System.out.println("Viewing/editing bookings...");
    }
}