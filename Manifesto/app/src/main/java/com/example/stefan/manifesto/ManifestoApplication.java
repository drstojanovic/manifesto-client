package com.example.stefan.manifesto;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

public class ManifestoApplication extends Application{

    private static ManifestoApplication application;
    private static boolean messagingActivityActive;
    private static int activeChatInterlocutor;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        application = this;
    }

    public static Context getContext() {
        return application.getApplicationContext();
    }

    public static boolean isIsMessagingActivityActive() {
        return messagingActivityActive;
    }

    public static void messagingActivityResumed(int activeUserChat) {
        messagingActivityActive = true;
        ManifestoApplication.activeChatInterlocutor = activeUserChat;
    }

    public static void messagingActivityPaused() {
        messagingActivityActive = false;
        activeChatInterlocutor = -1;
    }

    public static int getActiveChatInterlocutor() {
        return activeChatInterlocutor;
    }
}
