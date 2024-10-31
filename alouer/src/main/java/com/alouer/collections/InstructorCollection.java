package com.alouer.collections;

import java.util.ArrayList;
import java.util.List;
import com.alouer.models.Instructor;

public class InstructorCollection {
    private static List<Instructor> instructors = new ArrayList<>();
    private static int nextId = 0;

    public static List<Instructor> getInstructors() {
        return instructors;
    }

    public static Instructor find(int id) {
        if (id < 0 || id >= instructors.size()) {
            return null;
        }
        return instructors.get(id);
    }

    public static boolean add(Instructor instructor) {
        instructor.setId(nextId);
        instructors.add(instructor);
        nextId++;
        return true;
    }

    public static Instructor validateCredentials(String email, String password) {
        for (Instructor instructor : instructors) {
            if (instructor.getEmail().equals(email) && instructor.getPassword().equals(password)) {
                instructor.setConnected(true);
                return instructor;
            }
        }
        return null;
    }
}
