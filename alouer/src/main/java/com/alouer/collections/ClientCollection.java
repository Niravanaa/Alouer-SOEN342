package com.alouer.collections;

import java.util.ArrayList;
import java.util.List;
import com.alouer.models.Client;

public class ClientCollection {
    private static List<Client> clients = new ArrayList<>();
    private static int nextId = 0;

    public static List<Client> getClients() {
        return clients;
    }

    public static Client find(int id) {
        if (id < 0 || id >= clients.size()) {
            return null;
        }
        return clients.get(id);
    }

    public static boolean add(Client client) {
        client.setId(nextId);
        clients.add(client);
        nextId++;
        return true;
    }

    public static Client validateCredentials(String email, String password) {
        for (Client client : clients) {
            if (client.getEmail().equals(email) && client.getPassword().equals(password)) {
                client.setConnected(true);
                return client;
            }
        }
        return null;
    }
}
