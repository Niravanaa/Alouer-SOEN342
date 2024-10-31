package com.alouer.lessonManagement;

import java.util.*;
import java.util.ArrayList;
import com.alouer.enums.LessonType;
import com.alouer.enums.DayOfWeek;

public class Lesson {
    private int id;
    private LessonType type;
    private String title;
    private int locationId;
    private boolean isAvailable;
    private Date startTime;
    private Date endTime;
    private ArrayList<DayOfWeek> schedule;
    private Booking booking;

    public Lesson(LessonType type, String title, int locationId,
            Date startTime, Date endTime, ArrayList<DayOfWeek> schedule) {
        this.type = type;
        this.title = title;
        this.locationId = locationId;
        isAvailable = true;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public ArrayList<DayOfWeek> getSchedule() {
        return schedule;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
