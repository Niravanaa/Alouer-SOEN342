package com.alouer.lessonManagement;

import java.time.LocalTime;
import java.util.*;

import java.util.List;
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
            LocalTime startTime, LocalTime endTime, List<DayOfWeek> schedule) {
        this.type = type;
        this.title = title;
        this.locationId = locationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule;
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

    public Integer getLocationId() {
        return locationId;
    }

    public Integer getAssignedInstructorId() {
        return this.assignedInstructorId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public void setAvailable(Boolean available) {
        this.isAvailable = available;
    }

    public void setAssignedInstructorId(Integer assignedInstructorId) {
        this.assignedInstructorId = assignedInstructorId;
    }

    public boolean isAvailable() {
        return this.isAvailable;
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
