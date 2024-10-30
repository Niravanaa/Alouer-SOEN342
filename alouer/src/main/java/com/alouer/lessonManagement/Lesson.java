import java.time.*;
import java.util.ArrayList;
import java.util.Random;

public class Lesson {
    private int id;
    private LessonType type;
    private String title;
    private int locationId;
    private boolean isAvailable;
    private LocalTime startTime;
    private LocalTime endTime;
    private ArrayList<DayOfWeek> schedule;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Booking booking; // set to null?

    public Lesson(LessonType type, String title, int locationId,
            LocalTime startTime, LocalTime endTime, ArrayList<DayOfWeek> schedule) {
        this.type = type;
        this.title = title;
        this.locationId = locationId;
        isAvailable = true;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule; // TODO implement deep copy
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public LessonType getType() {
        return type;
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

    public ArrayList<DayOfWeek> getSchedule() {
        return schedule;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
