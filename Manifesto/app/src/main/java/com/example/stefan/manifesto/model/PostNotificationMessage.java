package com.example.stefan.manifesto.model;

public class PostNotificationMessage {

    private Integer postId;
    private Integer userId;
    private Integer eventId;
    private Double postLatitude;
    private Double postLongitude;
    private String message;
    private String type;


    public PostNotificationMessage() {

    }

    public PostNotificationMessage(Integer postId, Integer userId, Integer eventId, Double postLatitude,
                                   Double postLongitude, String message, String type) {
        super();
        this.postId = postId;
        this.userId = userId;
        this.eventId = eventId;
        this.postLatitude = postLatitude;
        this.postLongitude = postLongitude;
        this.message = message;
        this.type = type;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Double getPostLatitude() {
        return postLatitude;
    }

    public void setPostLatitude(Double postLatitude) {
        this.postLatitude = postLatitude;
    }

    public Double getPostLongitude() {
        return postLongitude;
    }

    public void setPostLongitude(Double postLongitude) {
        this.postLongitude = postLongitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

