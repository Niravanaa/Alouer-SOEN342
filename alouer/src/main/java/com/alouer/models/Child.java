package com.alouer.models;

import java.time.LocalDate;

public class Child {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Integer parentId;

    public Child(String firstName, String lastName, LocalDate dateOfBirth, Integer parentId) {
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

    public Integer getParentId() {
        return parentId;
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

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
