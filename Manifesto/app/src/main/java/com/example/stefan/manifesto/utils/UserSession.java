package com.example.stefan.manifesto.utils;

import com.example.stefan.manifesto.model.User;

public class UserSession {

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser (User newUser) {
        user = newUser;
    }



}
