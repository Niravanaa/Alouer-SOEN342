package com.alouer.collections;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.alouer.enums.DayOfWeek;
import com.alouer.enums.LessonType;
import com.alouer.lessonManagement.Lesson;
import com.alouer.utils.BackendUtils;
import com.alouer.utils.DatabaseManager;

public class LessonCollection {
    private static final String INSERT_LESSON_SQL = "INSERT INTO lesson (title, type, locationId, startTime, endTime) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_LESSONS_SQL = "SELECT * FROM lesson";
    private static final String SELECT_LESSON_BY_ID_SQL = "SELECT * FROM lesson WHERE id = ?";
    private static final String SELECT_LESSONS_BY_INSTRUCTOR_ID_SQL = "SELECT * FROM lesson WHERE instructor_id = ?";
    private static final String SELECT_SCHEDULE_BY_LESSON_ID_SQL = "SELECT dayOfWeek FROM lesson_schedule WHERE lessonId = ?";

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
                        getScheduleByLessonId(resultSet.getInt("id")));
                lesson.setAssignedInstructorId(resultSet.getInt("assignedInstructorId"));
                lesson.setId(resultSet.getInt("id"));
                lessons.add(lesson);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return lessons;
    }

    public static Set<DayOfWeek> getScheduleByLessonId(int lessonId) {
        List<DayOfWeek> schedule = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_SCHEDULE_BY_LESSON_ID_SQL)) {

            statement.setInt(1, lessonId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    schedule.add(DayOfWeek.valueOf(resultSet.getString("dayOfWeek")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        schedule.sort(Comparator.comparingInt(day -> Arrays.asList(DayOfWeek.values()).indexOf(day)));

        return new LinkedHashSet<>(schedule);
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
                            getScheduleByLessonId(resultSet.getInt(id)));
                    lesson.setAssignedInstructorId(resultSet.getInt("assignedInstructorId"));
                    lesson.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return lesson;
    }

    public static boolean add(Lesson lesson) {
        Connection connection = null;
        PreparedStatement lessonStatement = null;
        PreparedStatement scheduleStatement = null;

        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Insert the lesson
            lessonStatement = connection.prepareStatement(INSERT_LESSON_SQL, Statement.RETURN_GENERATED_KEYS);
            lessonStatement.setString(1, lesson.getTitle());
            lessonStatement.setString(2, lesson.getType().name());
            lessonStatement.setInt(3, lesson.getLocationId());
            lessonStatement.setString(4, lesson.getStartTime().toString());
            lessonStatement.setString(5, lesson.getEndTime().toString());

            int rowsInserted = lessonStatement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = lessonStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        lesson.setId(generatedKeys.getInt(1)); // Set generated ID
                    }
                }

                // Insert schedule entries
                String insertScheduleSQL = "INSERT INTO lesson_schedule (lessonId, dayOfWeek) VALUES (?, ?)";
                scheduleStatement = connection.prepareStatement(insertScheduleSQL);

                for (DayOfWeek day : lesson.getSchedule()) {
                    scheduleStatement.setInt(1, lesson.getId());
                    scheduleStatement.setString(2, day.name());
                    scheduleStatement.addBatch(); // Add to batch
                }

                scheduleStatement.executeBatch(); // Execute all schedule inserts
                connection.commit(); // Commit transaction
                return true;
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace(); // Handle rollback exceptions
                }
            }
            e.printStackTrace(); // Handle insert exceptions
        } finally {
            try {
                if (scheduleStatement != null)
                    scheduleStatement.close();
                if (lessonStatement != null)
                    lessonStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle closing exceptions
            }
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
                Set<DayOfWeek> existingDays = existingLesson.getSchedule();

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
            Set<DayOfWeek> dayOfWeeks = BackendUtils.parseSchedule(schedule);

            Lesson lesson = new Lesson(lessonType, title, locationId, start, end, dayOfWeeks);
            return add(lesson);
        } catch (Exception e) {
            System.out.println("Error creating lesson: " + e.getMessage());
            return false;
        }
    }

    public static List<Lesson> getUnassignedLessons(int locationId) {
        return getLessons().stream()
                .filter(lesson -> lesson.getLocationId() == locationId)
                .filter(lesson -> lesson.getAssignedInstructorId() == null)
                .filter(lesson -> BookingCollection.getBookings().stream()
                        .noneMatch(booking -> booking.getLessonId() == lesson.getId()))
                .collect(Collectors.toList());
    }

    public static List<Lesson> getAvailableLessons(int locationId) {
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
