package com.alouer.collections;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.alouer.models.Child;
import com.alouer.utils.DatabaseManager;

public class ChildCollection {
    private static final String INSERT_CHILD_SQL = "INSERT INTO child (firstName, lastName, dateOfBirth, parentId) VALUES (?, ?, ?, ?)";
    private static final String SELECT_CHILD_BY_ID_SQL = "SELECT * FROM child WHERE id = ?";
    private static final String SELECT_CHILDREN_BY_CLIENT_ID_SQL = "SELECT * FROM child WHERE parentId = ?";
    private static final String VALIDATE_CHILD_SQL = "SELECT * FROM child WHERE parentId = ? AND firstName = ? AND lastName = ? AND dateOfBirth = ?";
    private static final String SELECT_ALL_CHILDREN_SQL = "SELECT * FROM child";

    public static List<Child> getChildren() {
        List<Child> children = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CHILDREN_SQL);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Child child = new Child(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getDate("dateOfBirth").toLocalDate(),
                        resultSet.getInt("parentId"));
                child.setId(resultSet.getInt("id"));
                children.add(child);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return children;
    }

    public static Child getById(int id) {
        Child child = null;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_CHILD_BY_ID_SQL)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    child = new Child(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getDate("dateOfBirth").toLocalDate(),
                            resultSet.getInt("parentId"));
                    child.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return child;
    }

    public static boolean add(Child child) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_CHILD_SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, child.getFirstName());
            statement.setString(2, child.getLastName());
            statement.setDate(3, Date.valueOf(child.getDateOfBirth()));
            statement.setInt(4, child.getParentId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        child.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validateChild(Integer clientId, String firstName, String lastName, LocalDate dateOfBirth) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(VALIDATE_CHILD_SQL)) {

            statement.setInt(1, clientId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setDate(4, Date.valueOf(dateOfBirth));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Integer createChild(Integer clientId, String firstName, String lastName, LocalDate dateOfBirth) {
        Child newChild = new Child(firstName, lastName, dateOfBirth, clientId);

        if (add(newChild)) {
            return newChild.getId();
        }
        return null;
    }

    public static List<Child> getChildrenByClientId(Integer clientId) {
        List<Child> children = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_CHILDREN_BY_CLIENT_ID_SQL)) {

            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Child child = new Child(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getDate("dateOfBirth").toLocalDate(),
                            resultSet.getInt("parentId"));
                    child.setId(resultSet.getInt("id"));
                    children.add(child);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return children;
    }
}
