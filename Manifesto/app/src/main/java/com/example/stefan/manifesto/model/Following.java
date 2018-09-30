package com.example.stefan.manifesto.model;

public class Following {

    private int id;
    private int userId;
    private int eventId;

    public Following() {

    }

    public Following(int id, int userId, int eventId) {
        super();
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


}
