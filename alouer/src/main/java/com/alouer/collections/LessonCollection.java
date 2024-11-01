package com.alouer.collections;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.alouer.enums.DayOfWeek;
import com.alouer.enums.LessonType;
import com.alouer.lessonManagement.Lesson;
import com.alouer.utils.BackendUtils;
import com.alouer.utils.DatabaseManager;

public class LessonCollection {
    private static final String INSERT_LESSON_SQL = "INSERT INTO lesson (title, type, locationId, startTime, endTime, schedule) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_LESSONS_SQL = "SELECT * FROM lesson";
    private static final String SELECT_LESSON_BY_ID_SQL = "SELECT * FROM lesson WHERE id = ?";
    private static final String SELECT_LESSONS_BY_INSTRUCTOR_ID_SQL = "SELECT * FROM lesson WHERE instructor_id = ?";

    public static List<Lesson> getLessons() {
        List<Lesson> lessons = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_LESSONS_SQL);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Lesson lesson = new Lesson(
                        LessonType.valueOf(resultSet.getString("type")),
                        resultSet.getString("title"),
                        resultSet.getInt("locationId"),
                        LocalTime.parse(resultSet.getString("startTime")),
                        LocalTime.parse(resultSet.getString("endTime")),
                        BackendUtils.parseSchedule(resultSet.getString("schedule")));
                lesson.setId(resultSet.getInt("id"));
                lessons.add(lesson);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return lessons;
    }

    public static Lesson getById(int id) {
        Lesson lesson = null;
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_LESSON_BY_ID_SQL)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    lesson = new Lesson(
                            LessonType.valueOf(resultSet.getString("type")),
                            resultSet.getString("title"),
                            resultSet.getInt("locationId"),
                            LocalTime.parse(resultSet.getString("startTime")),
                            LocalTime.parse(resultSet.getString("endTime")),
                            BackendUtils.parseSchedule(resultSet.getString("schedule")));
                    lesson.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return lesson;
    }

    public static boolean add(Lesson lesson) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_LESSON_SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, lesson.getTitle());
            statement.setString(2, lesson.getType().name());
            statement.setInt(3, lesson.getLocationId());
            statement.setString(4, lesson.getStartTime().toString());
            statement.setString(5, lesson.getEndTime().toString());
            statement.setString(6, BackendUtils.convertDaysToSchedule(lesson.getSchedule()));

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        lesson.setId(generatedKeys.getInt(1)); // Set generated ID
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return false;
    }

    public static List<Lesson> getByInstructorId(Integer instructorId) {
        List<Lesson> lessons = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_LESSONS_BY_INSTRUCTOR_ID_SQL)) {

            statement.setInt(1, instructorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Lesson lesson = new Lesson(
                            LessonType.valueOf(resultSet.getString("type")),
                            resultSet.getString("title"),
                            resultSet.getInt("locationId"),
                            LocalTime.parse(resultSet.getString("startTime")),
                            LocalTime.parse(resultSet.getString("endTime")),
                            BackendUtils.parseSchedule(resultSet.getString("schedule")));

                    lesson.setId(resultSet.getInt("id"));
                    lessons.add(lesson);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return lessons;
    }

    public static boolean validateLesson(int locationId, String startTime, String endTime, String schedule) {
        int startMinutes = BackendUtils.convertTimeToMinutes(startTime);
        int endMinutes = BackendUtils.convertTimeToMinutes(endTime);
        Set<DayOfWeek> requestedDays = BackendUtils.convertScheduleToDays(schedule);

        for (Lesson existingLesson : getLessons()) {
            if (existingLesson.getLocationId() == locationId) {
                List<DayOfWeek> existingDays = existingLesson.getSchedule();

                if (!requestedDays.isEmpty() && !existingDays.isEmpty()
                        && !Collections.disjoint(requestedDays, existingDays)) {
                    int existingStartMinutes = BackendUtils
                            .convertTimeToMinutes(existingLesson.getStartTime().toString());
                    int existingEndMinutes = BackendUtils.convertTimeToMinutes(existingLesson.getEndTime().toString());

                    if (startMinutes < existingEndMinutes && endMinutes > existingStartMinutes) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean createLesson(int locationId, String title, LessonType lessonType, String startTime,
            String endTime, String schedule) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            List<DayOfWeek> dayOfWeeks = BackendUtils.parseSchedule(schedule);

            Lesson lesson = new Lesson(lessonType, title, locationId, start, end, dayOfWeeks);
            return add(lesson); // Using the add method to insert into the database
        } catch (Exception e) {
            System.out.println("Error creating lesson: " + e.getMessage());
            return false;
        }
    }

    public static List<Lesson> getAvailableLessonsByLocationId(int locationId) {
        return getLessons().stream()
                .filter(lesson -> lesson.getLocationId() == locationId)
                .filter(lesson -> lesson.getAssignedInstructorId() != null)
                .filter(lesson -> lesson.getType() == LessonType.GROUP ||
                        (lesson.getType() == LessonType.PRIVATE && lesson.isAvailable()))
                .filter(lesson -> BookingCollection.getBookings().stream()
                        .noneMatch(booking -> booking.getLessonId() == lesson.getId()))
                .collect(Collectors.toList());
    }
}
