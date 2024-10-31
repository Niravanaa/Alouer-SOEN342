package com.alouer.models;

public class Administrator {
    private String firstName = "Admin";
    private String lastName = "User";
    private String email = "admin@example.com";
    private String password = "password123";
    private boolean isConnected = false;

    private static Administrator instance = null;

    private Administrator() {
    }

    public static Administrator getInstance() {
        if (instance == null) {
            instance = new Administrator();
        }
        return instance;
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

    public void setConnected(boolean connectionStatus) {
        this.isConnected = connectionStatus;
    }

    public boolean isConnected() {
        return this.isConnected;
    }
}
