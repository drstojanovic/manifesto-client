package com.example.stefan.manifesto.model;

import java.util.Date;

public class Message {

    private int id;
    private String text;
    private Date time;
    private int senderId;
    private int receiverId;

    public Message(int id, String text, Date time, int senderId, int receiverId) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
}
