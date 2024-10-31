package com.alouer.models;

import java.util.ArrayList;

public class Location {
    private int id;
    private String name;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private ArrayList<Integer> lessons;

    public Location(String name, String address, String city, String province,
            String postalCode) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        lessons = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public ArrayList<Integer> getLessons() {
        return lessons;
    }
}
