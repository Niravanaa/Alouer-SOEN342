package com.alouer.models;

import java.time.LocalDate;

public class Child {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int parentId;

    public Child(String firstName, String lastName, LocalDate dateOfBirth, int parentId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getParentId() {
        return parentId;
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

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
