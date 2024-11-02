package com.alouer.models;

import java.util.List;
import java.util.ArrayList;

public class Location {
    private Integer id;
    private Integer id;
    private String name;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private List<Integer> lessons;

    public Location(String name, String address, String city, String province,
            String postalCode) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        lessons = new ArrayList<>();
    }

    public Integer getId() {

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public List<Integer> getLessons() {
        return lessons;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
