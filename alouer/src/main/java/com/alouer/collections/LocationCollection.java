package com.alouer.collections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.alouer.models.Location;
import com.alouer.utils.DatabaseManager;

public class LocationCollection {
    private static final String INSERT_LOCATION_SQL = "INSERT INTO location (name, description) VALUES (?, ?)";
    private static final String SELECT_ALL_LOCATIONS_SQL = "SELECT * FROM location";
    private static final String SELECT_LOCATION_BY_ID_SQL = "SELECT * FROM location WHERE id = ?";

    public static boolean add(Location location) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_LOCATION_SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, location.getName());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        location.setId(generatedKeys.getInt(1)); // Set generated ID
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return false;
    }

    public static List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_LOCATIONS_SQL);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Location location = new Location(resultSet.getString("name"), resultSet.getString("address"),
                        resultSet.getString("city"), resultSet.getString("province"),
                        resultSet.getString("postalCode"));
                location.setId(resultSet.getInt("id"));
                locations.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return locations;
    }

    public static Location getById(int id) {
        Location location = null;
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_LOCATION_BY_ID_SQL)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    location = new Location(resultSet.getString("name"), resultSet.getString("address"),
                            resultSet.getString("city"), resultSet.getString("province"),
                            resultSet.getString("postalCode"));
                    location.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return location;
    }
}
