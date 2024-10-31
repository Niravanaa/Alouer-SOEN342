package com.alouer.models;

import java.util.Date;

public class Child {
    private int id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private int parentId;
    private Date createdAt;
    private Date updatedAt;
    private boolean isConnected;

    public Child(String firstName, String lastName, Date dateOfBirth, int parentId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.parentId = parentId;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.isConnected = false;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public int getParentId() {
        return parentId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
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

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
