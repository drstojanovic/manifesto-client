package com.example.stefan.manifesto.utils;

import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.NotificationsSettingsItem;
import com.example.stefan.manifesto.model.User;

import java.util.List;

public class UserSession {

    private static User user;
    private static List<Event> followedEvents;

    public static User getUser() {
        return user;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }

    public static void setFollowedEvents(List<Event> followedEvents) {
        UserSession.followedEvents = followedEvents;
        writeToSharedPrefsForFirstTime(followedEvents);
    }

    private static void writeToSharedPrefsForFirstTime(List<Event> followedEvents) {
        for (Event followedEvent : followedEvents) {
            if (SharedPrefsUtils.getInstance().getIntValue(Constants.NOTIF_SETTINGS_ + followedEvent.getId(), -1) == -1) {
                SharedPrefsUtils.getInstance().setValue(Constants.NOTIF_SETTINGS_ + followedEvent.getId(), NotificationsSettingsItem.Scope.ALL.ordinal());
            }
        }
    }

    public static List<Event> getFollowedEvents() {
        return followedEvents;
    }

    public static boolean isUserFollowingEvent(int eventId) {
        for (Event followedEvent : followedEvents) {
            if (followedEvent.getId().equals(eventId)) {
                return true;
            }
        }
        return false;
    }

}
