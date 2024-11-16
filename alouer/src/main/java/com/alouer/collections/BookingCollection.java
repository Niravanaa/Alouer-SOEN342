package com.alouer.collections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.alouer.enums.LessonType;
import com.alouer.models.lessonManagement.Booking;
import com.alouer.models.lessonManagement.Lesson;
import com.alouer.utils.DatabaseManager;

public class BookingCollection {
    private static final String INSERT_BOOKING_SQL = "INSERT INTO booking (clientId, lessonId, childId) VALUES (?, ?, ?)";
    private static final String SELECT_BOOKING_BY_ID_SQL = "SELECT * FROM booking WHERE id = ?";
    private static final String SELECT_BOOKINGS_BY_CLIENT_ID_SQL = "SELECT * FROM booking WHERE clientId = ?";
    private static final String SELECT_BOOKING_BY_LESSON_ID_SQL = "SELECT * FROM booking WHERE lessonId = ?";
    private static final String DELETE_BOOKING_SQL = "DELETE FROM booking WHERE id = ?";
    private static final String GET_ALL_BOOKINGS_SQL = "SELECT * FROM booking";

    public static List<Booking> getBookings() {
        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ALL_BOOKINGS_SQL);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer childId = (Integer) resultSet.getObject("childId");
                Booking booking = new Booking(
                        resultSet.getInt("clientId"),
                        resultSet.getInt("lessonId"),
                        childId);
                booking.setId(resultSet.getInt("id"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static Booking getById(Integer id) {
        Booking booking = null;

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_BOOKING_BY_ID_SQL)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer childId = (Integer) resultSet.getObject("childId");
                    booking = new Booking(
                            resultSet.getInt("clientId"),
                            resultSet.getInt("lessonId"),
                            childId);
                    booking.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                        booking.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validateBooking(Integer selectedLessonId, Integer clientId) {
        Lesson selectedLesson = LessonCollection.getById(selectedLessonId);

        if (selectedLesson == null) {
            return false;
        }

        if (selectedLesson.getAssignedInstructorId() == null) {
            return false;
        }

        List<Booking> existingBookings = getByLessonId(selectedLesson.getId());

        for (Booking booking : existingBookings) {
            if (booking.getClientId().equals(clientId)) {
                return false;
            }
        }

        if (selectedLesson.getType() == LessonType.PRIVATE && existingBookings.size() != 0) {
            return false;
        }

        List<Booking> existingClientBookings = getByClientId(clientId);

        for (Booking clientBooking : existingClientBookings) {
            Lesson clientLesson = LessonCollection.getById(clientBooking.getLessonId());

            if (clientLesson != null) {
                if (selectedLesson.getSchedule().stream().anyMatch(clientLesson.getSchedule()::contains)) {
                    if (selectedLesson.getStartTime().isBefore(clientLesson.getEndTime()) &&
                            clientLesson.getStartTime().isBefore(selectedLesson.getEndTime())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static Integer createBooking(Integer clientId, Integer lessonId, Integer childId) {
        Booking newBooking = new Booking(clientId, lessonId, childId);

        if (add(newBooking)) {
            return newBooking.getId();
        }
        return null;
    }

    public static List<Booking> getByClientId(Integer clientId) {
        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_BOOKINGS_BY_CLIENT_ID_SQL)) {

            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Integer childId = (Integer) resultSet.getObject("childId");
                    Booking booking = new Booking(
                            resultSet.getInt("clientId"),
                            resultSet.getInt("lessonId"),
                            childId);
                    booking.setId(resultSet.getInt("id"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static List<Booking> getByLessonId(Integer lessonId) {
        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_BOOKING_BY_LESSON_ID_SQL)) {

            statement.setInt(1, lessonId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Integer childId = (Integer) resultSet.getObject("childId");
                    Booking booking = new Booking(
                            resultSet.getInt("clientId"),
                            resultSet.getInt("lessonId"),
                            childId);
                    booking.setId(resultSet.getInt("id"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static boolean delete(Integer id) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_BOOKING_SQL)) {

            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
