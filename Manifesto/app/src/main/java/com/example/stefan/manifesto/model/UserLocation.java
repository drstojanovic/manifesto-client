package com.example.stefan.manifesto.model;

public class UserLocation {

    private int id;
    private double latitude;
    private double longitude;

    public UserLocation(int userId, double latitude, double longitude) {
        super();
        this.id = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserLocation() {

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}

