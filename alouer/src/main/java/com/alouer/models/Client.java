package com.alouer.models;

import java.util.ArrayList;
import java.util.List;
import com.alouer.enums.UserType;

public class Client {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserType role;
    private List<Integer> children;
    private List<Integer> bookings;

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = UserType.CLIENT;
        this.children = new ArrayList<>();
        this.bookings = new ArrayList<>();
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getRole() {
        return role;
    }

    public List<Integer> getChildren() {
        return children;
    }

    public List<Integer> getBookings() {
        return bookings;
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

    public void addChild(Integer childId) {
        this.children.add(childId);
    }

    public void addBooking(Integer bookingId) {
        this.bookings.add(bookingId);
    }

    public void removeBooking(Integer bookingId) {
        for (int i = 0; i < this.bookings.size(); i++) {
            if (this.bookings.get(i) == bookingId) {
                this.bookings.remove(i);
                break;
            }
        }
    }
}
