package com.alouer.models;

import java.util.ArrayList;
import com.alouer.enums.UserType;

public class Instructor {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserType role;
    private boolean isConnected;
    private ArrayList<Integer> lessons;

    public Instructor(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = UserType.INSTRUCTOR;
        this.isConnected = false;
        this.lessons = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getRole() {
        return role;
    }

    public ArrayList<Integer> getLessons() {
        return lessons;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    // Methods to manage lessons
    public void addLesson(int lessonId) {
        this.lessons.add(lessonId);
    }

    public void removeLesson(int lessonId) {
        this.lessons.remove(Integer.valueOf(lessonId));
    }
}
