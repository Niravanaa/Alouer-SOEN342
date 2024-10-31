package com.alouer.collections;

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

public class LessonCollection {
    private static List<Lesson> lessons = new ArrayList<>();
    private static int nextId = 0;

    public static List<Lesson> getLessons() {
        return lessons;
    }

    public static Lesson find(int id) {
        for (Lesson lesson : lessons) {
            if (lesson.getId() == id) {
                return lesson;
            }
        }
        return null;
    }

    public static boolean add(Lesson lesson) {
        lesson.setId(nextId);
        lessons.add(lesson);
        nextId++;
        return true;
    }

    public static boolean validateLesson(int locationId, String startTime, String endTime, String schedule) {
        int startMinutes = BackendUtils.convertTimeToMinutes(startTime);
        int endMinutes = BackendUtils.convertTimeToMinutes(endTime);

        Set<DayOfWeek> requestedDays = BackendUtils.convertScheduleToDays(schedule);

        for (Lesson existingLesson : lessons) {
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

            add(lesson);

            return true;
        } catch (Exception e) {
            System.out.println("Error creating lesson: " + e.getMessage());
            return false;
        }
    }

    public static List<Lesson> getAvailableLessonsByLocationId(int locationId) {
        return lessons.stream()
                .filter(lesson -> lesson.getLocationId() == locationId)
                .filter(lesson -> lesson.getAssignedInstructorId() != -1)
                .filter(lesson -> lesson.getType() == LessonType.GROUP ||
                        (lesson.getType() == LessonType.PRIVATE && lesson.isAvailable()))
                .collect(Collectors.toList());
    }

    public static List<Lesson> filterByLocation(int locationId) {
        return lessons.stream().filter(lesson -> lesson.getLocationId() == locationId).collect(Collectors.toList());
    }
}
