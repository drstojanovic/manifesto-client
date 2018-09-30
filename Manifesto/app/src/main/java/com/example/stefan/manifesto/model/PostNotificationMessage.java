package com.example.stefan.manifesto.model;

public class PostNotificationMessage {

    private Integer postId;
    private String message;
    private String type;
    private Integer userId;


    public PostNotificationMessage() {

    }

    public PostNotificationMessage(Integer postId, String message, String type, Integer userId) {
        super();
        this.postId = postId;
        this.message = message;
        this.type = type;
        this.userId = userId;
    }

    public Integer getPostId() {
        return postId;
    }
    public void setPostId(Integer postId) {
        this.postId = postId;
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
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }


}
