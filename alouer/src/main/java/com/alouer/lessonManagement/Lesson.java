package com.alouer.lessonManagement;

import java.util.*;
import java.util.List;
import com.alouer.enums.LessonType;
import com.alouer.enums.DayOfWeek;
import java.time.LocalTime;

public class Lesson {
    private int id;
    private LessonType type;
    private String title;
    private int locationId;
    private int assignedInstructor = 0;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<DayOfWeek> schedule;
    private Booking booking;

    public Lesson(LessonType type, String title, int locationId,
            LocalTime startTime, LocalTime endTime, List<DayOfWeek> schedule) {
        this.type = type;
        this.title = title;
        this.locationId = locationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule; // TODO implement deep copy
    }

    public int getId() {
        return id;
    }

    public LessonType getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int assignedInstructor() {
        return assignedInstructor;
    }

    public void setAvailable(int assignedInstructor) {
        this.assignedInstructor = assignedInstructor;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<DayOfWeek> getSchedule() {
        return schedule;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
