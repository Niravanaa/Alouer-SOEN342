package com.alouer.collections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.alouer.models.Client;
import com.alouer.utils.DatabaseManager;

public class ClientCollection {
    private static final String INSERT_CLIENT_SQL = "INSERT INTO client (email, password, connected) VALUES (?, ?, ?)";
    private static final String SELECT_CLIENT_BY_ID_SQL = "SELECT * FROM client WHERE id = ?";
    private static final String SELECT_ALL_CLIENTS_SQL = "SELECT * FROM client";
    private static final String VALIDATE_CREDENTIALS_SQL = "SELECT * FROM client WHERE email = ? AND password = ?";
    private static final String DELETE_CLIENT_SQL = "DELETE FROM client WHERE id = ?";

    public static List<Client> getClients() {
        List<Client> clients = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CLIENTS_SQL);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        resultSet.getString("password"));
                client.setId(resultSet.getInt("id"));
                client.setConnected(resultSet.getBoolean("connected"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return clients;
    }

    public static Client getById(int id) {
        Client client = null;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_CLIENT_BY_ID_SQL)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    client = new Client(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password"));
                    client.setId(resultSet.getInt("id"));
                    client.setConnected(resultSet.getBoolean("connected"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return client;
    }

    public static boolean add(Client client) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_CLIENT_SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, client.getEmail());
            statement.setString(2, client.getPassword());
            statement.setBoolean(3, client.isConnected());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        client.setId(generatedKeys.getInt(1)); // Set generated ID
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return false;
    }

    public static Client validateCredentials(String email, String password) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(VALIDATE_CREDENTIALS_SQL)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Client client = new Client(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password"));
                    client.setId(resultSet.getInt("id"));
                    client.setConnected(true); // Set client as connected
                    return client;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return null;
    }

    public static boolean delete(int id) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_CLIENT_SQL)) {

            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0; // Returns true if a client was deleted
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return false;
    }
}
