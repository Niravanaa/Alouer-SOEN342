package com.alouer.collections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.alouer.models.Instructor;
import com.alouer.utils.DatabaseManager;

public class InstructorCollection {
    private static final String UPDATE_INSTRUCTOR_SQL = "UPDATE instructor SET firstName = ?, lastName = ?, email = ?, password = ?, phoneNumber = ? WHERE id = ?";
    private static final String INSERT_INSTRUCTOR_SQL = "INSERT INTO instructor (firstName, lastName, email, password, phoneNumber, role) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_INSTRUCTOR_BY_ID_SQL = "SELECT * FROM instructor WHERE id = ?";
    private static final String SELECT_INSTRUCTOR_BY_EMAIL_SQL = "SELECT * FROM instructor WHERE email = ?";
    private static final String SELECT_ALL_INSTRUCTORS_SQL = "SELECT * FROM instructor";
    private static final String VALIDATE_CREDENTIALS_SQL = "SELECT * FROM instructor WHERE email = ? AND password = ?";
    private static final String DELETE_INSTRUCTOR_SQL = "DELETE FROM instructor WHERE id = ?";

    public static List<Instructor> getInstructors() {
        List<Instructor> instructors = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_INSTRUCTORS_SQL);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Instructor instructor = new Instructor(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("phoneNumber"));
                instructor.setId(resultSet.getInt("id"));
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructors;
    }

    public static Instructor getById(Integer id) {
        Instructor instructor = null;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_INSTRUCTOR_BY_ID_SQL)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    instructor = new Instructor(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("phoneNumber"));
                    instructor.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructor;
    }

    public static Instructor getByEmail(String email) {
        Instructor instructor = null;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_INSTRUCTOR_BY_EMAIL_SQL)) {

            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    instructor = new Instructor(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("phoneNumber"));
                    instructor.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructor;
    }

    public static boolean createInstructor(String firstName, String lastName, String email, String password,
            String phoneNumber) {
        Instructor newInstructor = new Instructor(firstName, lastName, email, password, phoneNumber);
        return add(newInstructor);
    }

    public static boolean add(Instructor instructor) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_INSTRUCTOR_SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, instructor.getFirstName());
            statement.setString(2, instructor.getLastName());
            statement.setString(3, instructor.getEmail());
            statement.setString(4, instructor.getPassword());
            statement.setString(5, instructor.getRole().toString());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        instructor.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Instructor validateCredentials(String email, String password) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(VALIDATE_CREDENTIALS_SQL)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Instructor instructor = new Instructor(
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("phoneNumber"));
                    instructor.setId(resultSet.getInt("id"));
                    return instructor;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean delete(Integer id) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_INSTRUCTOR_SQL)) {

            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(Instructor instructor) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_INSTRUCTOR_SQL)) {

            statement.setString(1, instructor.getFirstName());
            statement.setString(2, instructor.getLastName());
            statement.setString(3, instructor.getEmail());
            statement.setString(4, instructor.getPassword());
            statement.setInt(5, instructor.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
