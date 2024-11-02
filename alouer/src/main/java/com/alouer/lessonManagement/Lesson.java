package com.alouer.lessonManagement;

import java.time.LocalTime;
import java.util.Set;

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
    private Set<DayOfWeek> schedule;
    private Integer bookingId;

    public Lesson(LessonType type, String title, int locationId,
            LocalTime startTime, LocalTime endTime, Set<DayOfWeek> schedule) {
        this.type = type;
        this.title = title;
        this.locationId = locationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule;
        this.isAvailable = false;
    }

    public Integer getId() {
        return id;
    }

    public LessonType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public Integer getAssignedInstructorId() {
        return this.assignedInstructorId;
    }

    public Set<DayOfWeek> getSchedule() {
        return schedule;
    }

    public Integer getBooking() {
        return bookingId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public void setAvailable(Boolean available) {
        this.isAvailable = available;
    }

    public void setAssignedInstructorId(Integer assignedInstructorId) {
        this.assignedInstructorId = assignedInstructorId;
        this.isAvailable = true;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}
