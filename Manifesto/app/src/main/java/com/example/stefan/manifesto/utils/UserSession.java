package com.example.stefan.manifesto.utils;

import com.example.stefan.manifesto.model.User;

import java.util.List;

public class UserSession {

    private static User user;
    private static List<Integer> followedEvents;

    public static User getUser() {
        return user;
    }

    public static void setUser (User newUser) {
        user = newUser;
    }

    public static void setFollowedEvents(List<Integer> followedEvents) {
        UserSession.followedEvents = followedEvents;
    }

    public static List<Integer> getFollowedEvents() {
        return followedEvents;
    }

    public static boolean isFollowingEvent(int eventId) {
        for (Integer followedEvent : followedEvents) {
            if (followedEvent.equals(eventId)) {
                return true;
            }
        }
        return false;
    }
}
