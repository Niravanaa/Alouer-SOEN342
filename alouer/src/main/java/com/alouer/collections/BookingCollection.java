package com.alouer.collections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.alouer.enums.LessonType;
import com.alouer.lessonManagement.Booking;
import com.alouer.lessonManagement.Lesson;
import com.alouer.models.Client;
import com.alouer.utils.DatabaseManager;

public class BookingCollection {
    private static final String INSERT_BOOKING_SQL = "INSERT INTO booking (clientId, lessonId, childId) VALUES (?, ?, ?)";
    private static final String SELECT_BOOKING_BY_ID_SQL = "SELECT * FROM booking WHERE id = ?";
    private static final String SELECT_BOOKINGS_BY_CLIENT_ID_SQL = "SELECT * FROM booking WHERE clientId = ?";
    private static final String SELECT_BOOKING_BY_LESSON_ID_SQL = "SELECT * FROM booking WHERE lessonId = ?";
    private static final String DELETE_BOOKING_SQL = "DELETE FROM booking WHERE id = ?";

    public static List<Booking> getBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM booking";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getInt("clientId"),
                        resultSet.getInt("lessonId"),
                        resultSet.getInt("childId"));
                booking.setId(resultSet.getInt("id")); // Assuming your Booking class has setId method
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return bookings;
    }

    public static Booking get(int id) {
        Booking booking = null;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_BOOKING_BY_ID_SQL)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    booking = new Booking(
                            resultSet.getInt("clientId"),
                            resultSet.getInt("lessonId"),
                            resultSet.getInt("childId"));
                    booking.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return booking;
    }

    public static boolean add(Booking booking) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_BOOKING_SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, booking.getClientId());
            statement.setInt(2, booking.getLessonId());
            if (booking.getChildId() != null) {
                statement.setInt(3, booking.getChildId());
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);
            }

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        booking.setId(generatedKeys.getInt(1)); // Set generated ID
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return false;
    }

    public static boolean validateBooking(Lesson selectedLesson, Client client) {
        if (!selectedLesson.isAvailable() || selectedLesson.getAssignedInstructorId() == null) {
            return false;
        }

        if (selectedLesson.getType() != LessonType.GROUP && selectedLesson.getType() != LessonType.PRIVATE) {
            return false;
        }

        Booking existingBooking = getByLessonId(selectedLesson.getId());
        return existingBooking == null; // True if no existing booking found
    }

    public static Integer createBooking(Integer clientId, Integer lessonId, Integer childId) {
        Booking newBooking = new Booking(clientId, lessonId, childId);

        if (add(newBooking)) {
            System.out.println("Booking created successfully for lesson ID: " + lessonId);
            return newBooking.getId();
        }
        return null;
    }

    public static List<Booking> getByClientId(int clientId) {
        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_BOOKINGS_BY_CLIENT_ID_SQL)) {

            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = new Booking(
                            resultSet.getInt("clientId"),
                            resultSet.getInt("lessonId"),
                            resultSet.getInt("childId"));
                    booking.setId(resultSet.getInt("id"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return bookings;
    }

    public static Booking getByLessonId(Integer lessonId) {
        Booking booking = null;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_BOOKING_BY_LESSON_ID_SQL)) {

            statement.setInt(1, lessonId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    booking = new Booking(
                            resultSet.getInt("clientId"),
                            resultSet.getInt("lessonId"),
                            resultSet.getInt("childId"));
                    booking.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return booking;
    }

    public static boolean delete(int id) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_BOOKING_SQL)) {

            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0; // Return true if any row was deleted
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return false;
    }
}
