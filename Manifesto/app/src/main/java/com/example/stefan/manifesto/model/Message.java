package com.example.stefan.manifesto.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.stefan.manifesto.utils.UserSession;

import java.util.Date;

public class Message implements Parcelable {

    private int id;
    private String text;
    private Date time;
    private int senderId;
    private int receiverId;

    public Message() {

    }

    public Message(int id, String text, Date time, int senderId, int receiverId) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    protected Message(Parcel in) {
        id = in.readInt();
        text = in.readString();
        senderId = in.readInt();
        receiverId = in.readInt();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(text);
        parcel.writeInt(senderId);
        parcel.writeInt(receiverId);
    }

    public int getInterlocutorId() {
        return senderId == UserSession.getUser().getId() ? senderId : receiverId;
    }
}
