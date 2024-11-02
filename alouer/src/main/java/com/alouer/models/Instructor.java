package com.alouer.models;

import java.util.ArrayList;
import java.util.List;

import com.alouer.enums.UserType;

public class Instructor {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserType role;
    private List<Integer> lessons;

    public Instructor(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = UserType.INSTRUCTOR;
        this.lessons = new ArrayList<>();
    }

    public Integer getId() {
        return this.id;
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

    public List<Integer> getLessons() {
        return lessons;
    }

    public void setId(Integer id) {
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

    public void addLesson(Integer lessonId) {
        this.lessons.add(lessonId);
    }

    public void removeLesson(Integer lessonId) {
        this.lessons.remove(lessonId);
    }
}
