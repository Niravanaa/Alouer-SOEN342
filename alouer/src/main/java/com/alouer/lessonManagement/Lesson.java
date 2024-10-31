package com.alouer.lessonManagement;

import java.time.LocalTime;
import java.util.*;

import com.alouer.enums.LessonType;
import com.alouer.enums.DayOfWeek;

public class Lesson {
    private Integer id;
    private LessonType type;
    private String title;
    private Integer locationId;
    private Integer assignedInstructorId;
    private Boolean isAvailable;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<DayOfWeek> schedule;
    private Booking booking;

    public Lesson(LessonType type, String title, int locationId,
            LocalTime startTime, LocalTime endTime, List<DayOfWeek> list) {
        this.type = type;
        this.title = title;
        this.locationId = locationId;
        this.isAvailable = true;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = list;
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

    public Integer getAssignedInstructorId() {
        return this.assignedInstructorId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setAssignedInstructorId(int assignedInstructorId) {
        this.assignedInstructorId = assignedInstructorId;
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
