package com.alouer.collections;

import java.util.ArrayList;
import java.util.List;

import com.alouer.enums.LessonType;
import com.alouer.lessonManagement.Booking;
import com.alouer.lessonManagement.Lesson;
import com.alouer.models.Client;

public class BookingCollection {
    private static List<Booking> bookings = new ArrayList<>();
    private static int nextId = 0;

    public static List<Booking> getBookings() {
        return bookings;
    }

    public static Booking find(int id) {
        for (Booking booking : bookings) {
            if (booking.getId() == id) {
                return booking;
            }
        }
        return null;
    }

    public static boolean add(Booking booking) {
        booking.setId(nextId);
        bookings.add(booking);
        nextId++;
        return true;
    }

    public static boolean validateBooking(Lesson selectedLesson, Client client) {
        if (!selectedLesson.isAvailable() || selectedLesson.getAssignedInstructorId() == null) {
            return false;
        }

        if (selectedLesson.getType() != LessonType.GROUP &&
                selectedLesson.getType() != LessonType.PRIVATE) {
            return false;
        }

        for (Booking booking : bookings) {
            if (booking.getLessonId() == selectedLesson.getId()) {
                return false;
            }
        }

        return true;
    }

    public static void createBooking(Integer clientId, Integer lessonId) {
        Booking newBooking = new Booking(clientId, lessonId);

        add(newBooking);

        System.out.println("Booking created successfully for lesson ID: " + lessonId);
    }
}
