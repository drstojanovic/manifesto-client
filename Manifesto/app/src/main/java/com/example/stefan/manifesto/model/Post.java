package com.example.stefan.manifesto.model;


import java.util.Date;

public class Post {

    private int id;
    private String image;
    private String text;
    private Double latitude;
    private Double longitude;
    private Date time;
    private String type;
    private int eventId;
    private User user;

    public Post(int id, String image, String text, Double latitude, Double longitude, Date time, String type,
                int eventId, User user) {
        super();
        this.id = id;
        this.image = image;
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.type = type;
        this.eventId = eventId;
        this.user = user;
    }

    public Post() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}